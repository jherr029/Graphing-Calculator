package teamteam.graphing_calculator;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Altros on 3/7/2018.
 */

public class MemoryFunction {
    private String display;
    private String complete;

    public MemoryFunction() {
        display = null;
        complete = null;
    }

    public MemoryFunction(String display, String complete) {
        this.display = display;
        this.complete = complete;
    }

    public String getDisplay() {
        return display;
    }

    public String getComplete() {
        return complete;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> ret = new HashMap<>();

        ret.put("display", display);
        ret.put("complete", complete);

        return ret;
    }
}
