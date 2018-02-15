package teamteam.graphing_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Scanner;
import java.util.Stack;

public class ExpressionEvaluation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expression_evaluation);
    }

    //Dijkstra double stack calculation
    public static void Prefix_Evaluation(String[] args) {
        Stack<Character> ops = new Stack<Character>();
        Stack<Double> values = new Stack<Double>();

        Scanner sc = new Scanner(System.in);
        System.out.println("Prefix expression:");

        String expression = sc.nextLine();
        char[] ch = expression.toCharArray();
        sc.close();

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

                if(op == '+') {
                    tmpResult = values.pop() + tmpResult;
                } else if(op == '-') {
                    tmpResult = values.pop() - tmpResult;
                } else if(op == '*') {
                    tmpResult = values.pop() * tmpResult;
                } else if(op == '/') {
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

        System.out.println("Results:" + values.pop());
    }
}
