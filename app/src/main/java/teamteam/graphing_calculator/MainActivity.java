package teamteam.graphing_calculator;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    MathKeyboard mMathKeyboard;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomSheetBehavior sheetController;
    private LinearLayout mGraphToolView;

    private FunctionAdapter mFunctionAdapter;

    boolean changeFlag = false;

    public GraphHandler graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.keyboard_view);

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
        sheetController.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetController.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        /* Initialize Graph Tool Menu */
        mGraphToolView = findViewById(R.id.graph_tool_menu);
        mGraphToolView.setVisibility(View.GONE);

        initListeners();

        mFunctionAdapter = new FunctionAdapter(this);

//      MathKeyboard
        mMathKeyboard = new MathKeyboard(this, R.id.keyboard_view, R.xml.keyboard_layout);
//        mMathKeyboard.registerEditText(getCurrentFocus());
        // TODO add a regular e

//        View functionViewer = findViewById(R.id.function_list_item);

        View testing = findViewById(R.id.function_list_view);
        ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.function_list_view);
        FrameLayout mFrameLayout = (FrameLayout) mViewGroup.getChildAt(0);
//        EditText xEditText = (EditText) mFrameLayout.getChildAt(1);
//        mMathKeyboard.registerEditText(xEditText.findFocus());

        LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View functionView = mInflater.inflate(R.layout.function_list_item, mViewGroup, false);
        EditText mEditText = functionView.findViewById(R.id.func);

        Log.d("keyboard", "ListView: " + testing.getClass());
        Log.d("keyboard", "ViewGroup " + mViewGroup);
        Log.d("keyboard", "FrameLayout: " + mFrameLayout);
        Log.d("keyboard", "EditText: " + mEditText);


        mMathKeyboard.registerEditText(mEditText);
//      End of keyboard

        //Initialize graph handler
        this.graph = new GraphHandler(this);
        initFields();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("MA:onStart", "about to call checkUserStatus");
        checkUserStatus();

        ListView functionListView = findViewById(R.id.function_list_view);
        functionListView.setAdapter(mFunctionAdapter);

        // TODO: Add continuity for graph settings
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
    public void onBackPressed() {
       if (mMathKeyboard.isKeyboardVisible())
           mMathKeyboard.hideKeyboard();
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

        Log.d("keyboard", "is keyboard visible: " + mMathKeyboard.isKeyboardVisible());

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
                // Non-functional
                DebugSnackbar("Snapped to Origin");
                break;
            case R.id.switch_to_cartesian:
                mFunctionAdapter.changeGraphType(RegexInterpreter.GraphType.CARTESIAN);
                graph.change_type("Cartesian");
                findViewById(R.id.switch_to_cartesian).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_selected);
                findViewById(R.id.switch_to_polar).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_flat);
                DebugSnackbar("Now Validating Cartesian Functions");
                break;
            case R.id.switch_to_polar:
                mFunctionAdapter.changeGraphType(RegexInterpreter.GraphType.POLAR);
                graph.change_type("Polar");
                findViewById(R.id.switch_to_cartesian).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_flat);
                findViewById(R.id.switch_to_polar).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_selected);
                DebugSnackbar("Now Validating Polar Functions");
                break;
            case R.id.use_degrees:
                // Switch to using degrees
                findViewById(R.id.use_radians).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_flat);
                findViewById(R.id.use_degrees).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_selected);
                break;
            case R.id.use_radians:
                // Switch to reading radians
                findViewById(R.id.use_radians).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_selected);
                findViewById(R.id.use_degrees).setBackgroundResource(
                        R.drawable.soft_rectangle_background_button_flat);
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

       MenuItem signInItem  = menu.getItem(0);
       MenuItem signOutItem = menu.getItem(1);
       MenuItem uploadItem  = menu.getItem(2);
       MenuItem downItem    = menu.getItem(3);

       if (loginStatus) {
           Log.d("TGLBTN", "user is signed in\n" +
                   "Sign in button off" +
                   "Sing out button on");

           signInItem.setVisible(false);
           signOutItem.setVisible(true);
           uploadItem.setVisible(true);
           downItem.setVisible(true);

       } else {
           Log.d("TGLBTN", "user is signed out\n" +
                   "Sign in button on" +
                   "Sign out button off");

           signInItem.setVisible(true);
           signOutItem.setVisible(false);
           uploadItem.setVisible(false);
           downItem.setVisible(false);
       }


    }

    public boolean onNavigationItemSelectedListener(MenuItem item) {

        Intent userStatusChangeIntent = new Intent(this, LoginModule.class);

        switch (item.getItemId()) {
            case R.id.drawer_sign_in:
                Log.d("DRAWER",  "Sign in pressed");
                userStatusChangeIntent.putExtra("userStatus", "signIn");
                startActivity(userStatusChangeIntent);
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

    private void initListeners() {
        /* Initializing Buttons */
        findViewById(R.id.open_nav).setOnClickListener(this);
        findViewById(R.id.open_settings).setOnClickListener(this);
        findViewById(R.id.snap_to_origin).setOnClickListener(this);
        findViewById(R.id.expand_function_list).setOnClickListener(this);
        findViewById(R.id.collapse_function_list).setOnClickListener(this);

        findViewById(R.id.switch_to_cartesian).setOnClickListener(this);
        findViewById(R.id.switch_to_polar).setOnClickListener(this);

        findViewById(R.id.use_radians).setOnClickListener(this);
        findViewById(R.id.use_degrees).setOnClickListener(this);
    }

    private void initFields() {
        EditText field = findViewById(R.id.minX); field.setText(String.valueOf(graph.min_x));
        field = findViewById(R.id.maxX); field.setText(String.valueOf(graph.max_x));
        field = findViewById(R.id.minY); field.setText(String.valueOf(graph.min_y));
        field = findViewById(R.id.maxY); field.setText(String.valueOf(graph.max_y));
    }

    /** Use this function to get Strings from user input fields
     * @param id (e.g. R.id.func)
     * @valid_ids func : top function field
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
        if (!(findViewById(id) instanceof EditText)) return "View provided is not of type EditText";
        EditText view = findViewById(id);
        String value = view.getText().toString();
        Log.d(TAG, "String Extracted: " + value);
        return value;
    }

    public void update(View view){
        String nminx = extractValue(R.id.minX);
        String nmaxx = extractValue(R.id.maxX);
        String nminy = extractValue(R.id.minY);
        String nmaxy = extractValue(R.id.maxY);

        if(nminx.equals("") || nmaxx.equals("") || nminy.equals("") || nmaxy.equals("")){
            return;
        }


        graph.reset(mFunctionAdapter.getFunctions(),
                    Integer.parseInt(nmaxx),
                    Integer.parseInt(nminx),
                    Integer.parseInt(nmaxy),
                    Integer.parseInt(nminy)
        );

        onClick(findViewById(R.id.open_settings));
    }
}
