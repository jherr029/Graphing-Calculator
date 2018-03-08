package teamteam.graphing_calculator;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Altros on 2/15/2018.
 */

public class BasicCalculatorContents {
    private static BasicCalculatorContents inst = new BasicCalculatorContents();

    private ArrayList<String> usrInputArray = new ArrayList<>();

    private String usrInput = "";
    private double calcResult;
    private Boolean dispResult = false;
    private Boolean dispError = false;
    private Boolean newInput = true;
    private Double[] calcResult2 = new Double[1];

    private ArrayList<MemoryValue> memoryValues = new ArrayList<>();
    private Map<String, MemoryValue> userMemoryValues = new HashMap<>();

    private int unclosedParens = 0;

    private RegexInterpreter validator = new RegexInterpreter();

    private FirebaseController ctrl = FirebaseController.getInst();

    public static synchronized BasicCalculatorContents get() {
        return inst;
    }

    public boolean Connected() {
        return ctrl.Connected();
    }

    public void connect() {
        ctrl.connect();
    }

    private void InputArrayToString() {
        usrInput = "";

        Iterator<String> usrInputArrayItr = usrInputArray.iterator();

        while (usrInputArrayItr.hasNext()) {
            usrInput = usrInput + usrInputArrayItr.next();
        }
    }

    private void addToMemory(String equation, double result) {
        if (Connected()) {
            ctrl.pushMemoryValue(new MemoryValue(equation, result));
        }
        else {
            memoryValues.add(new MemoryValue(equation, result));
        }
    }

    private void calculateResult() {
        String fStr = usrInput.replaceAll("√", "sqrt")
                              .replaceAll("π", Double.toString(Math.PI))
                              .replaceAll("e", Double.toString(Math.E));
        if (validator.isValidFunction(fStr) && (new ExpressionEvaluation()).Prefix_Evaluation(fStr, calcResult2)) {
            calcResult = calcResult2[0];
            dispResult = true;
        }
        else {
            dispError = true;
        }
    }

    public void storeResult() {
        if (!bDispResult()) {
            return;
        }

        addToMemory(usrInput, calcResult);
    }

    public ArrayList<MemoryValue> recallMemory() {
        if (Connected()) {
            userMemoryValues = ctrl.getVals();

            for (MemoryValue mv : userMemoryValues.values()) {
                Log.d("BasicCalculatorContents", "recallMemory: " + mv.stringify());
            }

            return new ArrayList<>(ctrl.getVals().values());
        }

        return memoryValues;
    }

    public void clearMemory() {
        ctrl.clearMemoryValues();
    }

    public String getUsrInput() {
        return usrInput;
    }

    public double getCalcResult() {
        return calcResult;
    }

    public Boolean bDispResult() {
        return dispResult;
    }

    public Boolean bDispError() {
        return dispError;
    }

    public String addInput(String input) {
        if (newInput) {
            clear();
            newInput = false;
        }
        dispError = false;

        if (input.charAt(input.length() - 1) == '(') {
            unclosedParens += 1;
        }
        else if (input.charAt(input.length() - 1) == ')') {
            unclosedParens -= 1;
        }

        usrInputArray.add(input);
        usrInput = usrInput + input;

        return usrInput;
    }

    public void addParens() {
        if (usrInput.length() == 0) {
            addInput("(");
            return;
        }

        char lastChar = usrInput.charAt(usrInput.length() - 1);

        switch (lastChar) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'π':
                if (unclosedParens > 0) {
                    addInput(")");
                }

                break;
            case ')':
                if (unclosedParens > 0) {
                    addInput(")");
                }

                break;
            case '.':
                break;
            default:
                addInput("(");
                break;
        }
    }

    public String delete() {
        dispError = false;
        if (usrInputArray.size() > 0) {
            usrInputArray.remove(usrInputArray.size() - 1);
        }

        InputArrayToString();

        return usrInput;
    }

    public void clear() {
        usrInputArray.clear();

        dispResult = false;

        InputArrayToString();
    }

    public void equals() {
        calculateResult();

        newInput = dispResult;
    }
}
