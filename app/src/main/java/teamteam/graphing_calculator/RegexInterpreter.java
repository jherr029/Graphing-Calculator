package teamteam.graphing_calculator;

import android.util.Log;
import java.util.Stack;

/** Created by makloooo on 2/16/18 */

public class RegexInterpreter {

    private static final String TAG = "RegexInterpreter";

    private static final String func_regex = "(sin[(])|(cos[(])|(tan[(])|(abs[(])|(log[(])|(ln[(])";
    private static final String op_regex = "[-+*/]";
    private static final String term_regex = "([0-9]+|[x]|[e]|[pi])";

    /** CFG for functions **
     *  expr -> (expr) | func op expr | func
     *  func -> sin(expr) | cos(expr) | tan(expr) | abs(expr) | log(expr) | ln(expr) | term
     *  op   -> + | - | * | /
     *  term -> [0-9]+ | x | e | pi
     */

    private String mFunction = "";

    private enum Rule { EXPR, FUNC, OP, TERM }
    private class Production {
        Rule ruleType;
        String buffer;
        Production(Rule ruleType) {
            this.ruleType = ruleType;
            this.buffer = "";
        }
    }

    public boolean isValidFunction(String function) {

        Log.d(TAG, "Read Function: " + function);

        mFunction = function;

        // All we really have to check is if it passes an FSM
        return DFA();
    }

    // push onto stack as you go into the cfg, pop as you go out
    // if stack is empty at end of parsing, then is valid
    private boolean DFA() {
        Stack<Production> parser = new Stack<>();
        parser.push(new Production(Rule.EXPR));

        while (!mFunction.isEmpty()) {
            Production top = parser.peek();
            top.buffer += mFunction.charAt(0);
            Log.d(TAG, top.ruleType.toString() + " -> \'" + top.buffer
                    + "\' | mFunction -> \'" + mFunction + "\'");

            switch (top.ruleType) {
                case EXPR:
                    if (top.buffer.matches("[(]")) {
                        removeTerminal();
                        parser.push(new Production(Rule.EXPR));
                    } else if (top.buffer.matches("[(][)]")) {
                        removeTerminal();
                        parser.pop();
                        if (!mFunction.isEmpty()) parser.push(new Production(Rule.OP));
                    } else {
                        parser.pop();
                        parser.push(new Production(Rule.FUNC));
                    }
                    break;
                case FUNC: // FIXME: Remove terminals here somehow for trig functions
                    if (top.buffer.matches(func_regex)) {
                        parser.push(new Production(Rule.EXPR));
                    } else if (top.buffer.matches(func_regex+"[)]")) {
                        removeTerminal();
                        parser.pop(); // pop func, is completed function
                        if (!mFunction.isEmpty()) parser.push(new Production(Rule.OP));
                    } else {
                        parser.pop(); // pop func before pushing a term
                        parser.push(new Production(Rule.TERM));
                    }
                    break;
                case OP:
                    parser.pop();
                    if (top.buffer.matches(op_regex)) {
                        removeTerminal();
                        parser.push(new Production(Rule.EXPR));
                    }
                    break;
                case TERM:
                    removeTerminal();
                    if (!mFunction.isEmpty() && !(top.buffer + mFunction.charAt(0)).matches("[0-9]+")) {
                        if (top.buffer.matches(term_regex)) {
                            parser.pop(); // pop term
                            parser.push(new Production(Rule.OP));
                        }
                    }
                    else if (mFunction.isEmpty() && top.buffer.matches(term_regex)) {
                        parser.pop(); // pop term
                    }
                    break;
                default:
                    Log.d(TAG, "Default case reached, returning false.");
                    return false; // Something obviously went wrong.
            }
        }
        if (parser.isEmpty()) return true; //If there are still things on the stack, is false.
        Log.d(TAG, "Things left on the stack:");
        while (!parser.isEmpty()) {
            Log.d(TAG, parser.peek().ruleType.toString());
            parser.pop();
        }
        return false;
    }

    private void removeTerminal() {
        if (!mFunction.isEmpty()) mFunction = mFunction.substring(1);
        else {
            Log.d(TAG, "String is empty, cannot remove terminal");
            return;
        }
    }

    /*
    private boolean expr() {
        Log.d(TAG, "***** NEW EXPR *****");
        String buffer = "";
        while (!mFunction.isEmpty()) {
            buffer += mFunction.charAt(0); // concatenate char into parsed string
            Log.d(TAG, "EXPR - " + buffer);
            if (buffer.matches("[(]")) {
                mFunction = mFunction.substring(1); // delete, we hit and "used" a terminal
                if (!expr()) return false; // check valid expr production, dont carry over anything
            }
            else if (buffer.matches("[(][)]")) return true; // return if closed expression

            else if (func()) { // if a valid func, check if it is 'func op expr'
                if (!mFunction.isEmpty() && op()) { // if op then go into op()
                    return expr(); // final production either correct or false
                }
            }
        }
        return true;
    }

    private boolean func() {
        Log.d(TAG, "***** NEW FUNC *****");
        String buffer = "";
        while (!mFunction.isEmpty()) {
            buffer += mFunction.charAt(0);
            mFunction = mFunction.substring(1);
            Log.d(TAG, "FUNC - " + buffer);
            if (buffer.matches(func_regex) && !expr()) return false;
            else if (buffer.matches(func_regex + "[)]")) return true; // successful function
            else if (term(buffer)) return true; // legit terms are also successful functions
        }
        return true;
    }

    private boolean op() {
        // since it's a single character, just double check and return whether its valid
        String buffer = "";
        buffer += mFunction.charAt(0);
        mFunction = mFunction.substring(1);
        Log.d(TAG, "***** NEW OP *****");
        Log.d(TAG, "OP - " + buffer);
        Log.d(TAG, "Op = " + buffer.matches(op_regex));
        return buffer.matches(op_regex);
    }

    private boolean term(String context) {
        String buffer = context;
        Log.d(TAG, "***** NEW TERM *****");
        Log.d(TAG, "TERM - " + buffer);
        while ((buffer+mFunction.charAt(0)).matches("[0-9]+")) {
            buffer += mFunction.charAt(0);
            mFunction = mFunction.substring(1);
            Log.d(TAG, "TERM - " + buffer);
            if (mFunction.isEmpty()) break;
        }
        Log.d(TAG, "Term = " + buffer.matches(term_regex));
        return buffer.matches(term_regex);
    }*/
}
