package teamteam.graphing_calculator;

import android.app.Activity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.Math.*;


public class GraphHandler {

    private Activity mainact;
    private GraphView graph;
    private ExpressionEvaluation parser;
    private Map functions;
    private int min_x = 0;
    private int max_x = 50;
    private int min_y = 0;
    private int max_y = 50;

    public GraphHandler (Activity act) {

        this.mainact = act;
        graph = this.mainact.findViewById(R.id.graph);
        functions = new HashMap();
        parser = new ExpressionEvaluation();

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

    }

    private DataPoint[] gen_data (String func){

        String formfunc = func.replaceAll("sin","@");
        formfunc = formfunc.replaceAll("cos", "#");
        formfunc = formfunc.replaceAll("tan", "$");
        formfunc = formfunc.replaceAll("sqrt", "&");

        //generates points from the function
        DataPoint[] points = new DataPoint[100];

        double step = (double)Math.abs(max_x - min_x)/100;

        for(int i = 0; i < 100; i++){
            double x_val = min_x + (i * step);
            String finalfunc = formfunc.replaceAll("x", Double.toString(x_val));
            double y_val = parser.Prefix_Parser(finalfunc);
            points[i] = new DataPoint(x_val, y_val);
        }

        return points;
    }

    //takes in a function in whatever format we're using, sends it to the point generator,
    //gets back array of DataPoint, stores points as LineGraphSeries, graphs line
    public void add_line(String func){
        //creates a series form the points
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(gen_data(func));
        //puts series into the list
        functions.put(func, series);
        //adds the series to the graph
        graph.addSeries((LineGraphSeries<DataPoint>)functions.get(func));
    }

    //takes in a function and removes it from the graph
    public void remove_line(String func){
        //removes series from the graph and redraws it
        graph.removeSeries((LineGraphSeries<DataPoint>)functions.get(func));
        //removes series from the list
        functions.remove(func);
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

        graph.onDataChanged(true,false);

        //regenerates lines
        Iterator it = functions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ((LineGraphSeries<DataPoint>)pair.getValue()).resetData(gen_data((String)pair.getKey()));
        }
    }

    public void reset(String func1, String func2, String func3, int nmaxx, int nminx, int nmaxy, int nminy){
        Iterator it = functions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            graph.removeSeries((LineGraphSeries<DataPoint>)pair.getValue());
            it.remove();
        }
        if(!func1.equals("")){
            add_line(func1);
        }
        if(!func2.equals("")){
            add_line(func2);
        }
        if(!func3.equals("")){
            add_line(func3);
        }
        update_bounds(nminx, nmaxx, nminy, nmaxy);

    }

}
