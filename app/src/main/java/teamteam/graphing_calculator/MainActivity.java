package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.*;

public class MainActivity extends AppCompatActivity {

    LoginModule mLoginModule;

    MainActivity() { mLoginModule = new LoginModule(this); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphView graph = (GraphView)findViewById(R.id.graph);

        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
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

        //mLoginModule.onCreate();

        // This is for the button. Uncomment this when you have a layout
        // findViewById(R.id.sign_in_button).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mLoginModule.onStart();
    }

    /*
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.sign_in_button) mLoginModule.signIn();
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginModule.onActivityResult(requestCode, resultCode, data);
    }
}
