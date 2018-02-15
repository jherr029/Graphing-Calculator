package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private BottomSheetBehavior sheetController;

    private RelativeLayout mGraphToolView;

    private GraphView mGraphView;

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


        /* Initializing Buttons */
        findViewById(R.id.open_nav).setOnClickListener(this);
        findViewById(R.id.open_settings).setOnClickListener(this);
        findViewById(R.id.snap_to_origin).setOnClickListener(this);
        findViewById(R.id.expand_function_list).setOnClickListener(this);
        findViewById(R.id.collapse_function_list).setOnClickListener(this);

        graphInit();
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
                DebugSnackbar("Snapped to Origin");
                Viewport viewport = mGraphView.getViewport();
                viewport.setMinX(-50);
                viewport.setMaxX(50);
                viewport.setMinY(-50);
                viewport.setMaxY(50);
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
            default:
        }
        return false;
    }

    private void DebugSnackbar(String text) {
        Snackbar.make(findViewById(R.id.main_content), text, Snackbar.LENGTH_SHORT).show();
    }

    private void graphInit() {
        mGraphView = (GraphView) findViewById(R.id.graph);

        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        // set manual X bounds
        mGraphView.getViewport().setYAxisBoundsManual(true);
        mGraphView.getViewport().setMinY(-150);
        mGraphView.getViewport().setMaxY(150);

        mGraphView.getViewport().setXAxisBoundsManual(true);
        mGraphView.getViewport().setMinX(4);
        mGraphView.getViewport().setMaxX(80);

        // enable scaling and scrolling
        mGraphView.getViewport().setScalable(true);
        mGraphView.getViewport().setScalableY(true);

        mGraphView.addSeries(series);
    }
}
