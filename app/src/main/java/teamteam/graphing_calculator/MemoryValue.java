package teamteam.graphing_calculator;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.NaN;

/**
 * Created by Altros on 3/7/2018.
 */

@IgnoreExtraProperties
class MemoryValue {
    private String equation;
    private double result;
    private long timestamp;

    public MemoryValue() {
        equation = null;
        result = NaN;
        timestamp = System.currentTimeMillis();
    }

    public MemoryValue(String eq, double res) {
        equation = eq;
        result = res;
        timestamp = System.currentTimeMillis();
    }

    public String getEquation() {
        return equation;
    }

    public double getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Exclude
    public String stringify() {
        //return key + ": " + equation + "=" + result;
        return equation + "=" + result;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> ret = new HashMap<>();

        ret.put("equation", equation);
        ret.put("result", result);
        ret.put("timestamp", timestamp);

        return ret;
    }
}
