package teamteam.graphing_calculator;

import android.util.Log;
import java.util.Stack;

/** Created by makloooo on 2/16/18 */

public class RegexInterpreter {

    private static final String TAG = "RegexInterpreter";

    private static final String func_regex = "((sin)|(cos)|(tan)|(cot)|(abs)|(lg)|(log)|(ln))[(]";
    private static final String op_regex = "[-+*/^]";
    private static final String term_regex = "-?(([0-9]+([.][0-9]+)?)|[x]|[e])";

    /**
     * CFG for functions **
     * expr -> [-]?(expr) | func op expr | func
     * func -> sin(expr) | cos(expr) | tan(expr) | abs(expr) | log(expr) | ln(expr) | (^[-]?)term
     * op   -> + | - | * | / | ^
     * term -> [0-9]+([.][0-9]+)? | x | e | pi
     */

    private String mFunction = "";

    private enum Rule {EXPR, FUNC, OP, TERM}

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

        // mFunction is
        mFunction = function.replaceAll("\\s+", "");

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
                    if (top.buffer.matches("-")) {
                        removeTerminal();
                    }
                    else if (top.buffer.matches("-?[(]")) { // '(' in '(expr)', push new expr
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

}