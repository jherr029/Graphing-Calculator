package teamteam.graphing_calculator;

import android.content.Intent;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

//    NavigationView nav_view = findViewById(R.id.navigation_view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        //mDrawerSignIn = findViewById(R.id.drawer_sign_in);
//        item.setVisible(false);
        //mDrawerSignOut = findViewById(R.id.drawer_sign_out);
        NavigationView nav_view = findViewById(R.id.navigation_view);
        nav_view.bringToFront();


        // Need to make sense out of this later
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(findViewById(R.id.content), menuItem.getTitle() + " pressed",
                        Snackbar.LENGTH_LONG).show();
                onNavigationItemSelectedListener(menuItem);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        Intent checkIfLoggedIn = new Intent( this, LoginModule.class);
        checkIfLoggedIn.putExtra("userStatusCheck", "checkUserStatus");
        startActivityForResult(checkIfLoggedIn, 100);
//        startActivityForResult(checkIfLoggedIn, 100);

        graphInit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity", "Inside onActivityResult");

        // If user is signed in, replace sign in button with sign out button
        // maybe delete requestCode
        if ( requestCode == 100 ) {
            if ( resultCode == RESULT_OK ) {

                boolean loginStatus = data.getBooleanExtra("loginStatus", false);

                NavigationView nav_view = findViewById(R.id.navigation_view);

                Menu menu = nav_view.getMenu();

                MenuItem signInItem = menu.getItem(0);
                MenuItem signOutItem = menu.getItem(1);

                if (loginStatus){
                    Log.d("Main Status", "user is signed in");

                    signInItem.setVisible(false);
                    signOutItem.setVisible(true);

//                    mDrawerSignIn.setVisibility(View.GONE);
//                    findViewById(R.id.drawer_sign_out).setVisibility(View.VISIBLE);

                } else {

                    Log.d("Main Status", "user is signed out");

                    signInItem.setVisible(true);
                    signOutItem.setVisible(false);

//                    mDrawerSignIn.setVisibility(View.VISIBLE);
//                    findViewById(R.id.drawer_sign_out).setVisibility(View.GONE);
                }


            }
            // FIGURE OUT HOW TO GET TRUE OR FALSE

        }
    }

    public boolean onNavigationItemSelectedListener(MenuItem item) {

        // Testing
        Intent checkIfLoggedIn = new Intent( this, LoginModule.class);
        checkIfLoggedIn.putExtra("userStatusCheck", "checkUserStatus");
//        startActivityForResult(checkIfLoggedIn, 100);
//        // End Testing

        Intent userStatusChangeIntent = new Intent(this, LoginModule.class);

        switch (item.getItemId()) {
            case R.id.drawer_sign_in:
                Log.d("DRAWER",  "Sign in pressed");

                userStatusChangeIntent.putExtra("userStatus", "signIn");
                startActivity(userStatusChangeIntent);

//                startActivityForResult(checkIfLoggedIn, 100);

                // Make this activity return a bool
                // If true (then is logged in), then set button visibility
//                startActivity(new Intent(this, LoginModule.class));
//                findViewById(R.id.drawer_sign_in).setVisibility(View.GONE);
//                findViewById(R.id.drawer_sign_out).setVisibility(View.VISIBLE);
                return true;

            case R.id.drawer_sign_out:
                Log.d("DRAWER", "Sign out pressed");

                userStatusChangeIntent.putExtra("userStatus", "signOut");
                startActivity(userStatusChangeIntent);

                startActivityForResult(checkIfLoggedIn, 100);

                // If false (then is not logged in), then set button visibility
//                startActivity(new Intent(MainActivity.this, LoginModule.class));
//                findViewById(R.id.drawer_sign_in).setVisibility(View.VISIBLE);
//                findViewById(R.id.drawer_sign_out).setVisibility(View.GONE);
                return true;

            case R.id.drawer_calculate:
                // Start Calculator Activity
                startActivity(new Intent(MainActivity.this, CalculateActivity.class));
                return true;

            default:
        }

        return false;
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
