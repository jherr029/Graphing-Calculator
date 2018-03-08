package teamteam.graphing_calculator;

import android.icu.text.LocaleDisplayNames;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

/** Created by makloooo on 2/16/18 */

public class RegexInterpreter {

    private static final String TAG = "RegexInterpreter";

    private static final String func_regex = "((sin)|(cos)|(tan)|(cot)|(abs)|(log)|(ln)|(sqrt))[(]";
    static final String op_regex = "[-+*/^]";
    private static final String cartesian_regex = "-?(([0-9]+([.][0-9]+)?)|[x]|[e]|[π])";
    private static final String polar_regex = "-?(([0-9]+([.][0-9]+)?)|[Θ]|[e]|[π])";

    enum GraphType {CARTESIAN, PARAMETRIC, POLAR}
    public GraphType mGraphType = GraphType.CARTESIAN;
    private String term_regex;

    private FunctionAdapter mContext;

    /**
     * CFG for functions **
     * expr -> [-]?(expr) | func op expr | func
     * func -> sin(expr) | cos(expr) | tan(expr) | abs(expr) | log(expr) | ln(expr) | (^[-]?)term
     * op   -> + | - | * | / | ^
     * term -> [0-9]+([.][0-9]+)? | x | e | pi
     */

    private String mFunction = "";

    private enum Rule {EXPR, FUNC, OP, TERM, NACPT}

    private class Production {
        Rule ruleType;
        String buffer;

        Production(Rule ruleType) {
            this.ruleType = ruleType;
            this.buffer = "";
        }
    }

    public RegexInterpreter(FunctionAdapter context) {
        mContext = context;
    }

    public boolean isValidFunction(String function) {

        // Remove all whitespace in function
        mFunction = function.replaceAll("\\s+", "");

        Log.d(TAG, "@@@ New Function : " + mFunction + " @@@");

        switch (mGraphType) {
            case CARTESIAN: term_regex = cartesian_regex; break;
            case PARAMETRIC: break;
            case POLAR: term_regex = polar_regex; break;
            default: return false;
        }

        if (mContext.getFunctions().contains(function)) return false;

        // All we really have to check is if it passes an FSM
        return DFA();
    }

    // push onto stack as you go into the cfg, pop as you go out
    // if stack is empty at end of parsing, then is valid
    private boolean DFA() {
        Stack<Production> parser = new Stack<>();
        parser.push(new Production(Rule.EXPR));

        DFAMainLoop : while (!mFunction.isEmpty() && !parser.isEmpty()) {
            Production top = parser.peek();
            top.buffer += mFunction.charAt(0);
            Log.d(TAG, top.ruleType.toString() + " -> \'" + top.buffer
                    + "\' | mFunction -> \'" + mFunction + "\'");

            switch (top.ruleType) {
                case EXPR:
                    if (top.buffer.matches("-")) {
                        removeTerminal();
                    } else if (top.buffer.matches("-?[(]")) { // '(' in '(expr)', push new expr
                        removeTerminal();
                        parser.push(new Production(Rule.EXPR));
                    } else if (top.buffer.matches("-?[(][)]")) { // ')' in '(expr)', pop expr
                        removeTerminal();
                        parser.pop();
                        if (!mFunction.isEmpty()) parser.push(new Production(Rule.OP));
                    } else {
                        parser.pop();
                        parser.push(new Production(Rule.FUNC));
                    }
                    break;
                case FUNC:
                    if (top.buffer.matches(func_regex)) {
                        removeTerminal();
                        parser.push(new Production(Rule.EXPR));
                    } else if (top.buffer.matches(func_regex + "[)]")) {
                        removeTerminal();
                        parser.pop(); // pop func, is completed function
                        if (!mFunction.isEmpty()) parser.push(new Production(Rule.OP));
                    } else if (top.buffer.matches(term_regex)) {
                        parser.pop(); // pop func before pushing a term
                        parser.push(new Production(Rule.TERM));
                    } else removeTerminal();
                    break;
                case OP:
                    parser.pop();
                    if (top.buffer.matches(op_regex)) {
                        removeTerminal();
                        parser.push(new Production(Rule.EXPR));
                    }
                    /*  Uncomment this to allow implicit multiplication
                    else if (top.buffer.matches(term_regex)) {
                        parser.push(new Production(Rule.TERM));
                    }
                    else if (top.buffer.matches("[(]")) {
                        parser.push(new Production(Rule.EXPR));
                    }
                    else if (!top.buffer.matches("[)]")) {
                        parser.push(new Production(Rule.EXPR));
                    } */
                    /* No acceptance of implicit multiplication */
                    else if (top.buffer.matches(term_regex) ||
                             top.buffer.matches("[(]") ||
                             !top.buffer.matches("[)]")) {
                        parser.push(new Production(Rule.NACPT));
                        break DFAMainLoop;
                    }
                    break;
                case TERM:
                    removeTerminal();
                    if (!mFunction.isEmpty() && !(top.buffer + mFunction.charAt(0)).matches("[0-9]+[.]?[0-9]?")) {
                        if (top.buffer.matches(term_regex)) {
                            parser.pop(); // pop term
                            parser.push(new Production(Rule.OP));
                        }
                    } else if (mFunction.isEmpty() && top.buffer.matches(term_regex)) {
                        parser.pop(); // pop term
                    }
                    break;
                default:
                    Log.d(TAG, "Default case reached, returning false.");
                    return false; // Something obviously went wrong.
            }
        }
        //If both resources don't empty at the same time, is false.
        if (parser.isEmpty() && mFunction.isEmpty()) return true;
        Log.d(TAG, "Productions left on the stack:");
        while (!parser.isEmpty()) {
            Log.d(TAG, parser.peek().ruleType.toString());
            parser.pop();
        }
        Log.d(TAG, "Function left to be parsed: " + mFunction);
        return false;
    }

    private void removeTerminal() {
        if (!mFunction.isEmpty()) mFunction = mFunction.substring(1);
        else {
            Log.d(TAG, "String is empty, cannot remove terminal");
            return;
        }
    }

    public void changeGraphType(GraphType newGraphType) {
        mGraphType = newGraphType;
        Log.d(TAG, "GraphType is now " + mGraphType.toString());
    }
}