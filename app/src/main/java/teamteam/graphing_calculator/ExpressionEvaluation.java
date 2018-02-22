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

    private static double result = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expression_evaluation);
    }

    //Dijkstra double stack calculation
    public static double Prefix_Parser(String arg) {
        /*
        Scanner sc = new Scanner(System.in);
        System.out.println("Prefix expression:");

        String expression = sc.nextLine();
        sc.close();

        Double result = 0.0;
        if (Prefix_Evaluation(expression, result)) {
            System.out.println("Results: " + result);
        } else {
            System.out.println("ERROR");
        }
        */
        Prefix_Evaluation(arg, 0.0);
        return result;
    }

    public static boolean Prefix_Evaluation(String expression, Double t) {
        expression = expression.toLowerCase();
        //System.out.println(expression);

        int posFRP = -1;
        while ((posFRP = expression.indexOf(')')) != -1) {
            int posLLP = expression.lastIndexOf('(', posFRP);
            Double subResult = 0.0;
            if (!Prefix_Evaluation(expression.substring(posLLP+1, posFRP), subResult)) {
                return false;
            }
            expression = expression.substring(0, posLLP) + subResult + expression.substring(posFRP+1, expression.length());
        }

        // Split expression by +, -, *, /
        Stack<Character> ops = new Stack<Character>();
        Stack<String> values = new Stack<String>();

        int a = 0;
        for (int i = 0; i < expression.length(); ++i) {
            Character c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
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
                temp_values.push(""+Math.sqrt(Double.valueOf(temp_value.substring(b+4))));
            }
            else if ((b = temp_value.indexOf("log_")) != -1) {
                // log_a_b
                a = temp_value.indexOf('_', b+4);
                //System.out.println("a - " + temp_value.substring(b+4, a));
                //System.out.println("b - " + temp_value.substring(a+1));
                temp_values.push(""+(Math.log(Double.valueOf(temp_value.substring(a+1))) / Math.log(Double.valueOf(temp_value.substring(b+4, a)))));
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
        result = Double.valueOf(values.pop());
        return true;
    }
}
/*
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

    //Dijkstra double stack calculation
    public static double Prefix_Evaluation(String arg) {
        Stack<Character> ops = new Stack<Character>();
        Stack<Double> values = new Stack<Double>();

        //Scanner sc = new Scanner(System.in);
        //System.out.println("Prefix expression:");

        //String expression = sc.nextLine();
        //char[] ch = expression.toCharArray();
        char[] ch = arg.toCharArray();
        //sc.close();

        for(int i = 0; i < ch.length; i++)
        {
            if(ch[i] == '+')
            {
                ops.push(ch[i]);
            }
            else if(ch[i] == '-')
            {
                ops.push(ch[i]);
            }
            else if(ch[i] == '*')
            {
                ops.push(ch[i]);
            }
            else if(ch[i] == '/')
            {
                ops.push(ch[i]);
            }
            //if left parenthesis, do nothing
            else if(ch[i] == '(')
            {
            }
            //if right parenthesis, pop the operands and operator, calculate the result and push it into stack
            else if(ch[i] == ')')
            {
                char op = ops.pop();
                Double tmpResult = values.pop();

                if(op == '+')
                {
                    tmpResult = values.pop() + tmpResult;
                }
                else if(op == '-')
                {
                    tmpResult = values.pop() - tmpResult;
                }
                else if(op == '*')
                {
                    tmpResult = values.pop() * tmpResult;
                }
                else if(op == '/')
                {
                    tmpResult = values.pop() / tmpResult;
                }
                values.push(tmpResult);
            }
            else
            //push all operands into stack
            {
                values.push(Double.parseDouble(Character.toString(ch[i])));
            }
        }

        //System.out.println("Results:" + values.pop());
        return values.pop();
    }
}
*/