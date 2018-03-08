package teamteam.graphing_calculator;

import android.graphics.Paint;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class PolarFunc {

    private List series;

    private double distance(double[] a, double[] b){
        return Math.sqrt(Math.pow(b[0]-a[0],2) + Math.pow(b[1]-a[1],2));
    }

    public PolarFunc(DataPoint[] points){
        series = new Vector();
        boolean done = false;
        List lines = new Vector();
        int pos = 0;

        //breaks points into lines
        while(!done){
            List currline = new Vector();
            currline.add(points[pos]);
            //if next point is in front
            if(points[pos+1].getX() >= points[pos].getX()){
                //iterate in +x
                while(points[pos+1].getX() >= points[pos].getX()){
                    currline.add(points[pos+1]);
                    pos++;
                    //if at end
                    if(pos + 1 == points.length){
                        double[] cpoint = new double[]{points[pos].getX(),points[pos].getY()};
                        double[] fpoint = new double[]{points[0].getX(),points[0].getY()};
                        double[] lpoint = new double[]{points[pos-1].getX(),points[pos-1].getY()};
                        if(distance(cpoint,fpoint) < 2*distance(cpoint,lpoint)){
                            currline.add(points[0]);
                        }
                        done = true;
                        break;
                    }
                }
                lines.add(currline);
                if(done){
                    break;
                }
            }
            //if next point is behind
            else{
                //iterate in -x
                while(points[pos+1].getX() < points[pos].getX()){
                    currline.add(points[pos+1]);
                    pos++;
                    //if at end
                    if(pos + 1 == points.length){
                        double[] cpoint = new double[]{points[pos].getX(),points[pos].getY()};
                        double[] fpoint = new double[]{points[0].getX(),points[0].getY()};
                        double[] lpoint = new double[]{points[pos-1].getX(),points[pos-1].getY()};
                        if(distance(cpoint,fpoint) < 2*distance(cpoint,lpoint)){
                            currline.add(points[0]);
                        }
                        done = true;
                        break;
                    }
                }
                lines.add(currline);
                if(done){
                    break;
                }
            }
        }

        //Comparator for DataPoints
        class SortPoint implements Comparator<DataPoint> {
            public int compare(DataPoint a, DataPoint b){
                if(a.getX() - b.getX() > 0){
                    return 1;
                }
                else if(a.getX() - b.getX() < 0){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }

        Paint p = new Paint();
        Random r = new Random();
        p.setARGB(255,r.nextInt(255),r.nextInt(255),r.nextInt(255));
        p.setStrokeWidth(5);

        //iterate through lines
        for(int i = 0; i < lines.size(); i++){
            List line = (List)lines.get(i);
            DataPoint[] sorted = new DataPoint[line.size()];
            Collections.sort(line, new SortPoint());
            //iterate through DataPoints
            for(int j = 0; j < line.size(); j++){
                sorted[j] = (DataPoint) line.get(j);
            }
            LineGraphSeries<DataPoint> s = new LineGraphSeries<>(sorted);
            s.setCustomPaint(p);
            series.add(s);
        }
    }

    public void add_to_graph(GraphView graph){
        for(int i = 0; i < series.size(); i++){
            graph.addSeries((LineGraphSeries<DataPoint>)series.get(i));
        }
    }

    public void remove_from_graph(GraphView graph){
        for(int i = 0; i < series.size(); i++){
            graph.removeSeries((LineGraphSeries<DataPoint>)series.get(i));
        }
    }

}
