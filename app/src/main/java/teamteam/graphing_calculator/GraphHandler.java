package teamteam.graphing_calculator;

import android.app.Activity;
import android.renderscript.ScriptGroup;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.Math.*;


public class GraphHandler {

    private Activity mainact;
    private GraphView graph;
    private ExpressionEvaluation parser;
    private Map functions;
    private double[] touchedpt = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
    private String touchedse = "";
    private double sens = 50;
    private int inc = 90;

    public int min_x = -30;
    public int max_x = 30;
    public int min_y = -45;
    public int max_y = 45;
    private String gtype = "Cartesian";

    private int lbound = 80;
    private int rbound = 1055;
    private int tbound = 20;
    private int bbound = 1670;

    //converts a pixel position to a position on the graph.
    //assumes position is within the bounds of the graph.
    private double[] getgraphpos (double[] loc){
        double[] pos = new double[2];

        double min_x = graph.getViewport().getMinX(false);
        double max_x = graph.getViewport().getMaxX(false);
        double min_y = graph.getViewport().getMinY(false);
        double max_y = graph.getViewport().getMaxY(false);

        //x val
        pos[0] = min_x + ((loc[0] - lbound)/(rbound - lbound)) * (max_x - min_x);
        //y val
        pos[1] = max_y - ((loc[1] - tbound)/(bbound - tbound)) * (max_y - min_y);
        return pos;
    }

    //converts a position on the graph to a pixel position
    private double[] getpixelpos (double[] loc){
        double[] pos = new double[2];

        double min_x = graph.getViewport().getMinX(false);
        double max_x = graph.getViewport().getMaxX(false);
        double min_y = graph.getViewport().getMinY(false);
        double max_y = graph.getViewport().getMaxY(false);

        pos[0] = (rbound - lbound) * ((loc[0] - min_x)/(max_x - min_x)) + lbound;
        pos[1] = (bbound - tbound) * ((loc[1] - min_y)/(max_y - min_y)) + tbound;

        return pos;
    }

    //evaluates distance between two points
    private double distance(double[] a, double[] b){
        return Math.sqrt(Math.pow(b[0]-a[0],2) + Math.pow(b[1]-a[1],2));
    }

    //returns the closest graphed point to the point touched and the series it is in.
    private String getclosest(double[] touch, double[] point){
        double dist = Double.POSITIVE_INFINITY;
        String series = "";
        //iterate through list of lines
        Iterator it = functions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            LineGraphSeries<DataPoint> s = (LineGraphSeries<DataPoint>)pair.getValue();
            //iterate through points on the line
            Iterator it1 = s.getValues(s.getLowestValueX(),s.getHighestValueX());
            while(it1.hasNext()){
                DataPoint dp = (DataPoint)it1.next();
                double[] pt = new double[]{dp.getX(),dp.getY()};
                //compute distance between touched point and the datapoint
                double d = distance(touch,pt);
                //if the datapoint is the closest so far
                if(d < dist){
                    //update shortest distance, closest point, and the series
                    dist = d;
                    point[0] = pt[0];
                    point[1] = pt[1];
                    series = (String)pair.getKey();
                }
            }
        }
        return series;
    }

    public GraphHandler (Activity act) {

        mainact = act;
        graph = mainact.findViewById(R.id.graph);
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

        graph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                //position of touched point in terms of pixels
                double[] touchpx = new double[]{ev.getX(),ev.getY()};
                //if point is not on the graph, it is ignored
                if(touchpx[0] < lbound || touchpx[0] > rbound || touchpx[1] < tbound || touchpx[1] > bbound){
                    graph.getViewport().setScrollable(true);
                    graph.getViewport().setScrollableY(true);
                    touchedpt = new double[]{Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
                    touchedse = "";
                    return false;
                }
                //position of touched point in terms of graph coordinates
                double[] touchgr = getgraphpos(touchpx);

                switch(ev.getAction()){
                    //user begins a touch
                    case MotionEvent.ACTION_DOWN: {
                        if(!functions.isEmpty()){
                            //closest datapoint to the touched point in terms of graph coordinates
                            double[] ptgr = new double[2];
                            //series containing closest datapoint
                            touchedse = getclosest(touchgr,ptgr);
                            //if series found
                            if(!touchedse.equals("")){
                                //you have to convert it back from touchgr for it to work.
                                //I dont know why.
                                touchpx = getpixelpos(touchgr);
                                //closest datapoint to the touched point in terms of pixels
                                double[] ptpx = getpixelpos(ptgr);
                                //if touched point is within sensitivity range of datapoint
                                if(distance(ptpx,touchpx) < sens){
                                    //System.out.println("Point touched");
                                    //turn off scrolling
                                    graph.getViewport().setScrollable(false);
                                    graph.getViewport().setScrollableY(false);
                                    touchedpt = ptgr;
                                }
                            }
                        }
                        break;
                    }
                    //user releases a touch
                    case MotionEvent.ACTION_UP: {
                        //turn scrolling back on
                        graph.getViewport().setScrollable(true);
                        graph.getViewport().setScrollableY(true);
                        //if a datapoint was touched previously
                        if(touchedpt[0] != Double.POSITIVE_INFINITY) {
                            //change in x
                            double deltax = touchgr[0] - touchedpt[0];
                            //change in y
                            double deltay = touchgr[1] - touchedpt[1];
                            //updated datapoints
                            DataPoint[] newdata = new DataPoint[inc];
                            //iterate through touched series
                            int i = 0;
                            LineGraphSeries s = (LineGraphSeries) functions.get(touchedse);
                            Iterator it = s.getValues(s.getLowestValueX(),s.getHighestValueX());
                            while(it.hasNext()){
                                DataPoint dp = (DataPoint)it.next();
                                //create updated datapoints
                                newdata[i] = new DataPoint(dp.getX()+deltax,dp.getY()+deltay);
                                i++;
                            }
                            //apply new datapoints
                            ((LineGraphSeries) functions.get(touchedse)).resetData(newdata);
                            touchedpt = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
                            touchedse = "";
                        }
                        break;
                    }
                    //touch fails
                    case MotionEvent.ACTION_CANCEL: {
                        graph.getViewport().setScrollable(true);
                        graph.getViewport().setScrollableY(true);
                        touchedpt = new double[]{Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
                        touchedse = "";
                        break;
                    }
                }

                return false;
            }
        });
    }

    private DataPoint[] gen_data (String func){

        //generates points from the function
        DataPoint[] points = new DataPoint[inc];
        func = func.replaceAll("pi",Double.toString(Math.PI));
        func = func.replaceAll("e",Double.toString(Math.E));

        if(gtype.equals("Cartesian")) {
            double step = (double) Math.abs(max_x - min_x) / inc;

            for (int i = 0; i < inc; i++) {
                double x_val = min_x + (i * step);
                String finalfunc = func.replaceAll("x", Double.toString(x_val));
                Double[] y_val = new Double[]{0.0};
                parser.Prefix_Evaluation(finalfunc, y_val);
                points[i] = new DataPoint(x_val, y_val[0]);
            }
        }
        else{
            //int inc = 50
            String theta = "Θ";
            double step = (2 * Math.PI) / inc;
            //x = r * cos(theta)
            String xfunc = "(" + func + ") * cos(" + theta + ")";
            //y = r * sin(theta)
            String yfunc = "(" + func + ") * sin(" + theta + ")";

            for(int i = 0; i < inc; i++){
                Double[] x_val = new Double[]{0.0};
                Double[] y_val = new Double[]{0.0};
                parser.Prefix_Evaluation(xfunc.replaceAll(theta, Double.toString(i * step)),
                                        x_val);
                parser.Prefix_Evaluation(yfunc.replaceAll(theta, Double.toString(i * step)),
                                        y_val);
                points[i] = new DataPoint(x_val[0], y_val[0]);
            }
        }

        return points;
    }

    //takes in a function in whatever format we're using, sends it to the point generator,
    //gets back array of DataPoint, stores points as LineGraphSeries, graphs line
    public void add_line(String func){
        if(gtype.equals("Cartesian")){
            //creates a series form the points
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(gen_data(func));
            //puts series into the list
            functions.put(func, series);
            //adds the series to the graph
            graph.addSeries((LineGraphSeries<DataPoint>)functions.get(func));
        }
        else{
            PolarFunc series = new PolarFunc(gen_data(func));
            functions.put(func, series);
            series.add_to_graph(graph);
        }

    }

    //takes in a function and removes it from the graph
    public void remove_line(String func){
        if (func.isEmpty() || functions.isEmpty()) return;
        if(gtype.equals("Cartesian")){
            //removes series from the graph and redraws it
            graph.removeSeries((LineGraphSeries<DataPoint>)functions.get(func));
            //removes series from the list
            functions.remove(func);
        }
        else{
            PolarFunc function = (PolarFunc)functions.get(func);
            if (function == null) return;
            function.remove_from_graph(graph);
            functions.remove(func);
        }
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

        if(gtype.equals("Cartesian")){
            //regenerates lines
            Iterator it = functions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                ((LineGraphSeries<DataPoint>)pair.getValue()).resetData(gen_data((String)pair.getKey()));
            }
        }

    }

    public void reset(ArrayList<String> functions, int nmaxx, int nminx, int nmaxy, int nminy){
        /*Iterator it = functions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if(gtype.equals("Cartesian")){
                graph.removeSeries((LineGraphSeries<DataPoint>)pair.getValue());
            }
            else{
                ((PolarFunc)pair.getValue()).remove_from_graph(graph);
            }
            it.remove();
        }*/
        update_bounds(nminx, nmaxx, nminy, nmaxy);

    }

    public void change_type(String type){
        if(!gtype.equals(type)){
            Iterator it = functions.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                if(gtype.equals("Cartesian")){
                    graph.removeSeries((LineGraphSeries<DataPoint>)pair.getValue());
                }
                else{
                    ((PolarFunc)pair.getValue()).remove_from_graph(graph);
                }
                it.remove();
            }
            gtype = type;
        }
    }
}