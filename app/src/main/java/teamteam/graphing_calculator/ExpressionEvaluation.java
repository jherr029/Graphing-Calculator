package teamteam.graphing_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Scanner;
import java.util.Stack;

//@ represent sin
//# represent cos
//$ represent tan
//& represent sqrt

public class ExpressionEvaluation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expression_evaluation);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Prefix expression:");

        String expression = sc.nextLine();
        sc.close();

        Double[] result = new Double[]{0.0};
        if (Prefix_Evaluation(expression, result)) {
            System.out.println("Results: " + result[0]);
        }
        else {
            System.out.println("ERROR");
        }
    }

    public static boolean Prefix_Evaluation(String expression, Double[] result) {
        expression = expression.toLowerCase();
        System.out.println(expression);

        int posFRP = -1;
        while ((posFRP = expression.indexOf(')')) != -1) {
            int posLLP = expression.lastIndexOf('(', posFRP);
            Double[] subResult = new Double[]{0.0};
            if (!Prefix_Evaluation(expression.substring(posLLP+1, posFRP), subResult)) {
                return false;
            }
            expression = expression.substring(0, posLLP) + subResult[0] + expression.substring(posFRP+1, expression.length());
        }
        System.out.println("After: " + expression);
        boolean check = false;
        do {
            check = false;
            int pos = -1;
            // If expression start by +, then get the substring after +
            if (expression.charAt(0) == '+') {
                expression = expression.substring(1);
                check = true;
            }
            // Replace -- or ++ to be +
            else if (((posFRP = expression.indexOf("--",0)) != -1) || ((posFRP = expression.indexOf("++",0)) != -1)) {
                expression = expression.substring(0, posFRP) + '+' + expression.substring(posFRP+2);
                check = true;
            }
            // Replace +- or -+ to be +
            else if (((posFRP = expression.indexOf("+-")) != -1) || ((posFRP = expression.indexOf("-+")) != -1)) {
                expression = expression.substring(0, posFRP) + '-' + expression.substring(posFRP+2);
                check = true;
            }
        } while (check);

        // Check the first char is + or not
        System.out.println("After2: " + expression);
        // Split expression by +, -, *, /
        Stack<Character> ops = new Stack<Character>();
        Stack<String> values = new Stack<String>();

        int a = 0;
        for (int i = 0; i < expression.length(); ++i) {
            Character c = expression.charAt(i);
            if (c == '+' || c == '*' || c == '/' || (c == '-' && i != 0 && Character.isDigit(expression.charAt(i-1)))) {
                ops.push(expression.charAt(i));
                values.push(expression.substring(a,i));
                a = i+1;
            }
        }
        values.push(expression.substring(a));

        /*
        System.out.println(expression + ":");
        while (!ops.empty()) {
            Character c = ops.pop();
            System.out.println(expression + " - ops - " + c);
        }
        while (!values.empty()) {
            String s = values.pop();
            System.out.println(expression + " - values - " + s);
        }
        */


        // Calculate sin, cos, ^, ...
        Stack<String> temp_values = new Stack<String>();
        while (!values.empty()) {
            String temp_value = values.pop();
            int b = -1;
            // Radian
            if ((b = temp_value.indexOf("sin")) != -1) {
                temp_values.push(""+Math.sin(Double.valueOf(temp_value.substring(b+3))));
            }
            else if ((b = temp_value.indexOf("cos")) != -1) {
                temp_values.push(""+Math.cos(Double.valueOf(temp_value.substring(b+3))));
            }
            else if ((b = temp_value.indexOf("tan")) != -1) {
                temp_values.push(""+Math.tan(Double.valueOf(temp_value.substring(b+3))));
            }
            else if ((b = temp_value.indexOf("cot")) != -1) {
                // 1 / tan
                temp_values.push(""+(1/Math.tan(Double.valueOf(temp_value.substring(b+3)))));
            }
            else if ((b = temp_value.indexOf('^')) != -1) {
                temp_values.push(""+Math.pow(Double.valueOf(temp_value.substring(0, b)), Double.valueOf(temp_value.substring(b+1))));
            }
            else if ((b = temp_value.indexOf("sqrt")) != -1) {
                Double temp = Double.valueOf(temp_value.substring(b+4));
                if (temp < 0) {
                    return false;
                }
                temp_values.push(""+Math.sqrt(temp));
            }
            else if ((b = temp_value.indexOf("log_")) != -1) {
                // log_a_b
                a = temp_value.indexOf('_', b+4);
                //System.out.println("a - " + temp_value.substring(b+4, a));
                //System.out.println("b - " + temp_value.substring(a+1));
                Double base = Double.valueOf(temp_value.substring(b+4, a));
                Double temp = Double.valueOf(temp_value.substring(a+1));
                if (base < 0 || temp <= 0) {
                    return false;
                }
                temp_values.push(""+(Math.log(temp) / Math.log(base)));
            }
            else if ((b = temp_value.indexOf("lg")) != -1) {
                // lg = log_10
                temp_values.push(""+(Math.log(Double.valueOf(temp_value.substring(b+2))) / Math.log(10)));
            }
            else if ((b = temp_value.indexOf("ln")) != -1) {
                // ln = log_e
                temp_values.push(""+Math.log(Double.valueOf(temp_value.substring(b+2))));
            }
            else {
                temp_values.push(temp_value);
            }
        }

        // Calculate *, /
        Stack<Character> temp_ops = new Stack<Character>();
        while (!ops.empty()) {
            temp_ops.push(ops.pop());
        }
        values.push(temp_values.pop());
        while (!temp_ops.empty()) {
            Character c = temp_ops.pop();
            if (c == '*') {
                Double x = Double.valueOf(values.pop());
                values.push("" + (x * Double.valueOf(temp_values.pop())));
            }
            else if (c == '/') {
                Double x = Double.valueOf(values.pop());
                Double y = Double.valueOf(temp_values.pop());
                if (y == 0.0) {
                    return false;
                }
                values.push("" + (x / y));
            }
            else {
                values.push(temp_values.pop());
                ops.push(c);
            }
        }

        // Calculate +, -
        while (!ops.empty()) {
            temp_ops.push(ops.pop());
            temp_values.push(values.pop());
        }
        while (!temp_ops.empty()) {
            Character c = temp_ops.pop();
            Double x = Double.valueOf(values.pop());
            if (c == '+') {
                values.push("" + (x + Double.valueOf(temp_values.pop())));
            }
            else if (c == '-') {
                values.push("" + (x - Double.valueOf(temp_values.pop())));
            }
        }

        /*
        System.out.println(expression + ":");
        while (!ops.empty()) {
            Character c = ops.pop();
            System.out.println(expression + " - ops - " + c);
        }
        while (!values.empty()) {
            String s = values.pop();
            System.out.println(expression + " - values - " + s);
        }
        */
        /*
        1+2+(3/sin(4+5^6*sqrt(7+8)))+9
        */
        if (values.empty()) {
            return false;
        }
        result[0] = Double.valueOf(values.pop());
        System.out.println(expression + " : " + result[0]);
        return true;
    }
}
