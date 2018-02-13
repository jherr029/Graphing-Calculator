package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
//import com.jjoe64.graphview.series.;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView nav_view = findViewById(R.id.navigation_view);
        nav_view.bringToFront();

        // Need to make sense out of this later
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(findViewById(R.id.content), menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                onNavigationItemSelectedListener(menuItem);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

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
    public void add_line(/* parameters TBD */){
        //generates points from the function
        DataPoint[] points = //insert point generation function here
        //creates a series form the points
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        //puts series into the list
        functions.put(/* function parameter */, series);
        //adds the series to the graph
        graph.addSeries(functions.get(/* function parameter */));
    }

    //takes in a function and removes it from the graph
    public void remove_line(/* parameters TBD */){
        //removes series from the graph and redraws it
        graph.removeSeries(functions.get(/* function parameter */));
        //removes series from the list
        functions.remove(/* function parameter */);
    }

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
            pair.getValue().resetData(/* call point generation function with pair.getKey() */);
        }
    }
}
