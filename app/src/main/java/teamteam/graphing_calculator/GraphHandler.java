package teamteam.graphing_calculator;

import android.app.Activity;
import android.renderscript.ScriptGroup;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
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

    public int min_x = -30;
    public int max_x = 30;
    public int min_y = -45;
    public int max_y = 45;
    private String gtype = "Cartesian";

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

        //generates points from the function
        int inc = 90;
        DataPoint[] points = new DataPoint[inc];

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

    public void setFunctions(ArrayList<String> new_functions) {
        graph.removeAllSeries();
        functions.clear();
        while (!new_functions.isEmpty()) {
            add_line(new_functions.get(0));
            new_functions.remove(0);
        }
    }
}
