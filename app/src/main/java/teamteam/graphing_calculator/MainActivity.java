package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
//import com.jjoe64.graphview.series.;
import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private BottomSheetBehavior sheetController;

    private LinearLayout mGraphToolView;

    private ExpressionEvaluation mFunctionParser;

//    NavigationView nav_view = findViewById(R.id.navigation_view);
    boolean changeFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialize Navigation Drawer */
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView nav_view = findViewById(R.id.navigation_view);
        nav_view.bringToFront();

        mNavigationView = findViewById(R.id.navigation_view);
        NavigationView.OnNavigationItemSelectedListener nav_listener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(findViewById(R.id.main_content), menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                onNavigationItemSelectedListener(menuItem);
                mDrawerLayout.closeDrawers();
                return true;
            }
        };
        mNavigationView.setNavigationItemSelectedListener(nav_listener);


        /* Initialize Function Sheet */
        sheetController = BottomSheetBehavior.from(findViewById(R.id.function_bottom_sheet));


        /* Initialize Graph Tool Menu */
        mGraphToolView = findViewById(R.id.graph_tool_menu);
        mGraphToolView.setVisibility(View.GONE);


        mFunctionParser = new ExpressionEvaluation();

        /* Initializing Buttons */
        findViewById(R.id.open_nav).setOnClickListener(this);
        findViewById(R.id.open_settings).setOnClickListener(this);
        findViewById(R.id.snap_to_origin).setOnClickListener(this);
        findViewById(R.id.expand_function_list).setOnClickListener(this);
        findViewById(R.id.collapse_function_list).setOnClickListener(this);

//        Intent checkIfLoggedIn = new Intent( this, LoginModule.class);
//        checkIfLoggedIn.putExtra("userStatusCheck", "checkUserStatus");
//        Log.d("MAIN", "Calling startActivityForResult from onCreate()");
//        startActivityForResult(checkIfLoggedIn, 100);

        graphInit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("MA:onStart", "about to call checkUserStatus");
        checkUserStatus();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("MA:onResume", "On Resume function");

        if (changeFlag) {
            checkUserStatus(); // this one not good

            changeFlag = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity", "Inside onActivityResult");

        // If user is signed in, replace sign in button with sign out button
        // maybe delete requestCode
        if ( requestCode == 100 ) {
            if ( resultCode == RESULT_OK ) {
                Log.d("REQUEST ACCEPTED","inside onActivityResult");

                dynamicButtonAction(data);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_nav:
                if (sheetController.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetController.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                mDrawerLayout.openDrawer(mNavigationView);
                mNavigationView.bringToFront();
                break;
            case R.id.open_settings:
                if (mGraphToolView.getVisibility() == View.VISIBLE) {
                    mGraphToolView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_anim));
                    mGraphToolView.setVisibility(View.GONE);
                } else if (mGraphToolView.getVisibility() == View.GONE) {
                    mGraphToolView.setVisibility(View.VISIBLE);
                    mGraphToolView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_anim));
                }

                break;
            case R.id.snap_to_origin:
                extractValue(R.id.func_1);
                break;
            case R.id.expand_function_list:
                sheetController.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.collapse_function_list:
                sheetController.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    public void dynamicButtonAction(Intent data)  {

        boolean loginStatus;

        if (data.getExtras() != null) {
            Log.d("DBA", "LoginStatus intent is not null\n" +
                    "Assumption: loginStatus == true" +
                    "OR loginStatus == false");

            loginStatus = data.getExtras().getBoolean("loginStatus");
        } else {
            Log.d("DBA", "loginStatus intent is null" +
                    "FORCING loginStatus == false");

            loginStatus = false;
        }

        Log.d("LoginStatus", "" + loginStatus);

        toggleButtons(loginStatus);

    }

    public void toggleButtons(boolean loginStatus) {
       NavigationView nav_view = findViewById(R.id.navigation_view);

       Menu menu = nav_view.getMenu();

       MenuItem signInItem = menu.getItem(0);
       MenuItem signOutItem = menu.getItem(1);

       if (loginStatus) {
           Log.d("TGLBTN", "user is signed in\n" +
                   "Sign in button off" +
                   "Sing out button on");

           signInItem.setVisible(false);
           signOutItem.setVisible(true);

       } else {
           Log.d("TGLBTN", "user is signed out\n" +
                   "Sign in button on" +
                   "Sign out button off");

           signInItem.setVisible(true);
           signOutItem.setVisible(false);
       }


    }

    public boolean onNavigationItemSelectedListener(MenuItem item) {

        Intent userStatusChangeIntent = new Intent(this, LoginModule.class);

        switch (item.getItemId()) {
            case R.id.drawer_sign_in:
                Log.d("DRAWER",  "Sign in pressed");

                userStatusChangeIntent.putExtra("userStatus", "signIn");
                startActivity(userStatusChangeIntent);

                activityActions.main();

//                checkUserStatus();

               return true;

            case R.id.drawer_sign_out:
                Log.d("DRAWER", "Sign out pressed");

                userStatusChangeIntent.putExtra("userStatus", "signOut");
                startActivity(userStatusChangeIntent);

                changeFlag = true;

                return true;

            case R.id.drawer_calculate_old:
                // Start Calculator Activity
                startActivity(new Intent(MainActivity.this, CalculateActivity.class));
                return true;
            case R.id.drawer_calculate:
                startActivity(new Intent(MainActivity.this, BasicCalculator.class));
                return true;
            case R.id.drawer_customize_expression:
                return true;
            case R.id.drawer_function_template:
                return true;
            default:
        }

        return false;
    }

    private void checkUserStatus() {

        Log.d("MainActivity", "inside checkUserStatus function\n" +
                "starting intent");

        Intent checkIfLoggedIn = new Intent( this, LoginModule.class);
        checkIfLoggedIn.putExtra("userStatusCheck", "checkUserStatus");

        startActivityForResult(checkIfLoggedIn, 100);

        Log.d("MainActivity", "activity has already started");

    }

    private void DebugSnackbar(String text) {
        Snackbar.make(findViewById(R.id.main_content), text, Snackbar.LENGTH_SHORT).show();
    }

    /** Use this function to get Strings from user input fields
     * @param id (e.g. R.id.func_1)
     * @valid_ids func_1 : top function field
     *            func_2 : middle function field
     *            func_3 : bottom function field
     *            min_X
     *            max_X
     *            min_Y
     *            max_Y
     * @return string of what is in the edit text field, you
     *         may want to cast the min_X ... max_Y fields to
     *         int/double to use them properly.
     */
    private String extractValue(int id) {
        if (!(findViewById(id) instanceof EditText)) return "Invalid String";
        EditText view = findViewById(id);
        String value = view.getText().toString();
        DebugSnackbar(value);
        return value;
    }

    private GraphView graph;
    private Map functions;
    private int min_x = -10;
    private int max_x = 10;
    private int min_y = -10;
    private int max_y = 10;

    private void graphInit() {
        graph = findViewById(R.id.graph);
        functions = new HashMap();
        /*
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        */

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(min_y);
        graph.getViewport().setMaxY(max_y);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(min_x);
        graph.getViewport().setMaxX(max_x);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        //graph.addSeries(series);
    }

    //takes in a function in whatever format we're using, sends it to the point generator,
    //gets back array of DataPoint, stores points as LineGraphSeries, graphs line
    //public void add_line(/* parameters TBD */){
        //generates points from the function
        //DataPoint[] points = //insert point generation function here
        //creates a series form the points
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        //puts series into the list
        //functions.put(/* function parameter */, series);
        //adds the series to the graph
        //graph.addSeries(functions.get(/* function parameter */));
    //}

    //takes in a function and removes it from the graph
    //public void remove_line(/* parameters TBD */){
        //removes series from the graph and redraws it
        //graph.removeSeries(functions.get(/* function parameter */));
        //removes series from the list
        //functions.remove(/* function parameter */);
    //}

    /*
    //updates the boundaries of the graph and regenerates the lines
    public void update_bounds(int minx, int maxx, int miny, int maxy){
        //updates variables
        min_x = minx;
        min_y = miny;
        max_x = maxx;
        max_y = maxy;
        //updates graph variables
        graph.getViewport().setMinX(min_x);
        graph.getViewport().setMaxX(max_x);
        graph.getViewport().setMinY(min_y);
        graph.getViewport().setMaxY(max_y);

        //regenerates lines
        Iterator it = functions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            pair.getValue().resetData(/* call point generation function with pair.getKey() );*/
       // }
    //}
}
