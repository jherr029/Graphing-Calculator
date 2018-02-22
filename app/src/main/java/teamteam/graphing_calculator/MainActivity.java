package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private class FunctionWatcher implements TextWatcher {
        private EditText mEditText;
        private ImageView mErrorIcon;

        private String prevFunction;
        public FunctionWatcher(FrameLayout layout) {
            this.mEditText = (EditText)layout.getChildAt(1);
            this.mErrorIcon = (ImageView)layout.getChildAt(2);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            prevFunction = mEditText.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString() == "") mErrorIcon.setVisibility(View.INVISIBLE);
            else if (mRegexInterpreter.isValidFunction(s.toString())) {
                // graph the function, remove any error icons
                //graph.remove_line(prevFunction);
                //graph.add_line(s.toString());
                mErrorIcon.setVisibility(View.INVISIBLE);
            }
            else {
                // dont graph or remove function, display error icon
                mErrorIcon.setVisibility(View.VISIBLE);
            }
        }
    };

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private BottomSheetBehavior sheetController;

    private LinearLayout mGraphToolView;

    private RegexInterpreter mRegexInterpreter;
    private ExpressionEvaluation mFunctionParser;

    private GraphHandler graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialize Navigation Drawer */
        mDrawerLayout = findViewById(R.id.drawer_layout);
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

        mRegexInterpreter = new RegexInterpreter();
        mFunctionParser = new ExpressionEvaluation();

        initListeners();

        //Initialize graph handler
        this.graph = new GraphHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                boolean valid = mRegexInterpreter.isValidFunction(extractValue(R.id.func_1));
                if (valid) DebugSnackbar("Function is valid");
                else DebugSnackbar("Function is invalid");
                break;
            case R.id.expand_function_list:
                sheetController.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.collapse_function_list:
                sheetController.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    public boolean onNavigationItemSelectedListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_sign_in:
                // Make this activity return a user
                // If true (then is logged in), then set button visibility
                startActivity(new Intent(MainActivity.this, LoginModule.class));
                findViewById(R.id.drawer_sign_in).setVisibility(View.GONE);
                findViewById(R.id.drawer_sign_out).setVisibility(View.VISIBLE);
                return true;
            case R.id.drawer_sign_out:
                // Make this return null if successful
                // If false (then is not logged in), then set button visibility
                startActivity(new Intent(MainActivity.this, LoginModule.class));
                findViewById(R.id.drawer_sign_in).setVisibility(View.VISIBLE);
                findViewById(R.id.drawer_sign_out).setVisibility(View.GONE);
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

        /* Initializing EditText Listeners, these are just temporary. */
        EditText textField = findViewById(R.id.func_1);
        textField.addTextChangedListener(new FunctionWatcher((FrameLayout)textField.getParent()));
        textField = findViewById(R.id.func_2);
        textField.addTextChangedListener(new FunctionWatcher((FrameLayout)textField.getParent()));
        textField = findViewById(R.id.func_3);
        textField.addTextChangedListener(new FunctionWatcher((FrameLayout)textField.getParent()));
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

        graph.reset(extractValue(R.id.func_1),
                    extractValue(R.id.func_2),
                    extractValue(R.id.func_3),
                    Integer.parseInt(nmaxx),
                    Integer.parseInt(nminx),
                    Integer.parseInt(nmaxy),
                    Integer.parseInt(nminy)
        );
    }

}
