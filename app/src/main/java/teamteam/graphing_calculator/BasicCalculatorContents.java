package teamteam.graphing_calculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Altros on 2/15/2018.
 */

public class BasicCalculatorContents {
    private static BasicCalculatorContents inst = new BasicCalculatorContents();

    private List<String> usrInputArray = new ArrayList<>();

    private String usrInput = "";
    private double calcResult;
    private Boolean dispResult = false;
    private Boolean newInput = true;

    private int unclosedParens = 0;

    public static synchronized BasicCalculatorContents get() {
        return inst;
    }

    private void InputArrayToString() {
        usrInput = "";

        Iterator<String> usrInputArrayItr = usrInputArray.iterator();

        while (usrInputArrayItr.hasNext()) {
            usrInput = usrInput + usrInputArrayItr.next();
        }
    }

    private void calculateResult() {
        Random r = new Random();

        calcResult = r.nextDouble();

        dispResult = true;
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

    public String addInput(String input) {
        if (newInput) {
            clear();
            newInput = false;
        }

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
            case '!':
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
        usrInputArray.remove(usrInputArray.size() - 1);

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

        newInput = true;
    }
}
