package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.bringToFront();

        // Need to make sense out of this later
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(findViewById(R.id.main_content), menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                onNavigationItemSelectedListener(menuItem);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        /* Initializing Buttons */
        findViewById(R.id.open_nav).setOnClickListener(this);

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
                mDrawerLayout.openDrawer(mNavigationView);
                break;
            case R.id.open_settings:
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
            case R.id.drawer_calculate:
                // Start Calculator Activity
                startActivity(new Intent(MainActivity.this, CalculateActivity.class));
                return true;
            default:
        }
        return false;
    }

    private void DebugSnackbar(String text) {
        Snackbar.make(findViewById(R.id.main_content), text, Snackbar.LENGTH_LONG).show();
    }

    private void graphInit() {
        GraphView graph = (GraphView) findViewById(R.id.graph);

        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);
    }
}
