package teamteam.graphing_calculator;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Atticus on 2/26/18.
 */

public class PolarFunc {

    private List series;

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
                        currline.add(points[0]);
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
                        currline.add(points[0]);
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


        //iterate through lines
        for(int i = 0; i < lines.size(); i++){
            List line = (List)lines.get(i);
            DataPoint[] sorted = new DataPoint[line.size()];
            Collections.sort(line, new SortPoint());
            //iterate through DataPoints
            for(int j = 0; j < line.size(); j++){
                //DataPoint temp1 = (DataPoint) line.get(j);
                sorted[j] = (DataPoint) line.get(j);
            }
            LineGraphSeries<DataPoint> s = new LineGraphSeries<>(sorted);
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
