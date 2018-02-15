package teamteam.graphing_calculator;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class BasicCalculator extends AppCompatActivity implements View.OnClickListener {

    private static final String ADD = "+";
    private static final String SUB = "-";
    private static final String MUL = "*";
    private static final String DIV = "/";

    private static final String LPAREN = "(";
    private static final String RPAREN = ")";

    private static final String SQRT = "√(";
    private static final String EXP = "e^(";
    private static final String POW = "^(";
    private static final String LN = "ln(";
    private static final String LOG = "log(";

    private static final String SIN = "sin(";
    private static final String COS = "cos(";
    private static final String TAN = "tan(";

    private static final String ASIN = "asin(";
    private static final String ACOS = "acos(";
    private static final String ATAN = "atan(";

    private static final String DECIMAL = ".";
    private static final String FACTORIAL = "!";

    private static final String EQUALS = "=";

    private BasicCalculatorContents calculatorContents = BasicCalculatorContents.get();

    private DecimalFormat decimalFormat;

    private TextView txtEquation;
    private TextView txtResult;

    private void SetPortraitButtons() {
        Button btnClear = (Button) findViewById(R.id.btnClear);
        Button btnDivide = (Button) findViewById(R.id.btnDivide);
        Button btnMultiply = (Button) findViewById(R.id.btnMultiply);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        Button btnSeven = (Button) findViewById(R.id.btnSeven);
        Button btnEight = (Button) findViewById(R.id.btnEight);
        Button btnNine = (Button) findViewById(R.id.btnNine);
        Button btnMinus = (Button) findViewById(R.id.btnMinus);
        Button btnFour = (Button) findViewById(R.id.btnFour);
        Button btnFive = (Button) findViewById(R.id.btnFive);
        Button btnSix = (Button) findViewById(R.id.btnSix);
        Button btnPlus = (Button) findViewById(R.id.btnPlus);
        Button btnOne = (Button) findViewById(R.id.btnOne);
        Button btnTwo = (Button) findViewById(R.id.btnTwo);
        Button btnThree = (Button) findViewById(R.id.btnThree);
        Button btnParen = (Button) findViewById(R.id.btnParen);
        Button btnZero = (Button) findViewById(R.id.btnZero);
        Button btnDecimal = (Button) findViewById(R.id.btnDecimal);
        Button btnNegate = (Button) findViewById(R.id.btnNegate);
        Button btnEquals = (Button) findViewById(R.id.btnEquals);

        btnClear.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnParen.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnDecimal.setOnClickListener(this);
        btnNegate.setOnClickListener(this);
        btnEquals.setOnClickListener(this);
    }

    private void SetLandscapeButtons() {
        Button btnFacotrial = (Button) findViewById(R.id.btnFactorial);
        Button btnSqrt = (Button) findViewById(R.id.btnSqrt);
        Button btnPow = (Button) findViewById(R.id.btnPow);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        Button btnDivide = (Button) findViewById(R.id.btnDivide);
        Button btnMultiply = (Button) findViewById(R.id.btnMultiply);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        Button btnSine = (Button) findViewById(R.id.btnSine);
        Button btnCosine = (Button) findViewById(R.id.btnCosine);
        Button btnTangent = (Button) findViewById(R.id.btnTangent);
        Button btnSeven = (Button) findViewById(R.id.btnSeven);
        Button btnEight = (Button) findViewById(R.id.btnEight);
        Button btnNine = (Button) findViewById(R.id.btnNine);
        Button btnMinus = (Button) findViewById(R.id.btnMinus);
        Button btnArcsine = (Button) findViewById(R.id.btnArcsine);
        Button btnArccosine = (Button) findViewById(R.id.btnArccosine);
        Button btnArctangent = (Button) findViewById(R.id.btnArctangent);
        Button btnFour = (Button) findViewById(R.id.btnFour);
        Button btnFive = (Button) findViewById(R.id.btnFive);
        Button btnSix = (Button) findViewById(R.id.btnSix);
        Button btnPlus = (Button) findViewById(R.id.btnPlus);
        Button btnLog = (Button) findViewById(R.id.btnLog);
        Button btnLn = (Button) findViewById(R.id.btnLn);
        Button btnExp = (Button) findViewById(R.id.btnExp);
        Button btnOne = (Button) findViewById(R.id.btnOne);
        Button btnTwo = (Button) findViewById(R.id.btnTwo);
        Button btnThree = (Button) findViewById(R.id.btnThree);
        Button btnParen = (Button) findViewById(R.id.btnParen);
        Button btnMS = (Button) findViewById(R.id.btnMS);
        Button btnMC = (Button) findViewById(R.id.btnMC);
        Button btnMR = (Button) findViewById(R.id.btnMR);
        Button btnZero = (Button) findViewById(R.id.btnZero);
        Button btnDecimal = (Button) findViewById(R.id.btnDecimal);
        Button btnNegate = (Button) findViewById(R.id.btnNegate);
        Button btnEquals = (Button) findViewById(R.id.btnEquals);

        btnFacotrial.setOnClickListener(this);
        btnSqrt.setOnClickListener(this);
        btnPow.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSine.setOnClickListener(this);
        btnCosine.setOnClickListener(this);
        btnTangent.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnArcsine.setOnClickListener(this);
        btnArccosine.setOnClickListener(this);
        btnArctangent.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnLog.setOnClickListener(this);
        btnLn.setOnClickListener(this);
        btnExp.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnParen.setOnClickListener(this);
        btnMS.setOnClickListener(this);
        btnMC.setOnClickListener(this);
        btnMR.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnDecimal.setOnClickListener(this);
        btnNegate.setOnClickListener(this);
        btnEquals.setOnClickListener(this);
    }

    private void SetLayout(Configuration Config) {
        switch (Config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                SetPortraitButtons();
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                SetLandscapeButtons();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        decimalFormat = new DecimalFormat("#.##########");

        txtEquation = (TextView) findViewById(R.id.txtEquation);
        txtResult = (TextView) findViewById(R.id.txtResult);

        txtEquation.setText(calculatorContents.getUsrInput());
        if (calculatorContents.bDispResult()) {
            txtResult.setText(EQUALS + decimalFormat.format(calculatorContents.getCalcResult()));
        }
        else {
            txtResult.setText("");
        }

        SetLayout(getResources().getConfiguration());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnFactorial:
                calculatorContents.addInput(FACTORIAL);
                break;
            case R.id.btnSqrt:
                calculatorContents.addInput(SQRT);
                break;
            case R.id.btnPow:
                calculatorContents.addInput(POW);
                break;
            case R.id.btnClear:
                calculatorContents.clear();
                break;
            case R.id.btnDivide:
                calculatorContents.addInput(DIV);
                break;
            case R.id.btnMultiply:
                calculatorContents.addInput(MUL);
                break;
            case R.id.btnDelete:
                calculatorContents.delete();
                break;
            case R.id.btnSine:
                calculatorContents.addInput(SIN);
                break;
            case R.id.btnCosine:
                calculatorContents.addInput(COS);
                break;
            case R.id.btnTangent:
                calculatorContents.addInput(TAN);
                break;
            case R.id.btnSeven:
                calculatorContents.addInput("7");
                break;
            case R.id.btnEight:
                calculatorContents.addInput("8");
                break;
            case R.id.btnNine:
                calculatorContents.addInput("9");
                break;
            case R.id.btnMinus:
                calculatorContents.addInput(SUB);
                break;
            case R.id.btnArcsine:
                calculatorContents.addInput(ASIN);
                break;
            case R.id.btnArccosine:
                calculatorContents.addInput(ACOS);
                break;
            case R.id.btnArctangent:
                calculatorContents.addInput(ATAN);
                break;
            case R.id.btnFour:
                calculatorContents.addInput("4");
                break;
            case R.id.btnFive:
                calculatorContents.addInput("5");
                break;
            case R.id.btnSix:
                calculatorContents.addInput("6");
                break;
            case R.id.btnPlus:
                calculatorContents.addInput(ADD);
                break;
            case R.id.btnLog:
                calculatorContents.addInput(LOG);
                break;
            case R.id.btnLn:
                calculatorContents.addInput(LN);
                break;
            case R.id.btnExp:
                calculatorContents.addInput(EXP);
                break;
            case R.id.btnOne:
                calculatorContents.addInput("1");
                break;
            case R.id.btnTwo:
                calculatorContents.addInput("2");
                break;
            case R.id.btnThree:
                calculatorContents.addInput("3");
                break;
            case R.id.btnParen:
                calculatorContents.addParens();
                break;
            case R.id.btnMS:
            case R.id.btnMC:
            case R.id.btnMR:
            case R.id.btnZero:
                calculatorContents.addInput("0");
                break;
            case R.id.btnDecimal:
                calculatorContents.addInput(DECIMAL);
                break;
            case R.id.btnNegate:
                calculatorContents.addInput(SUB);
                break;
            case R.id.btnEquals:
                calculatorContents.equals();
                break;
            default:
                calculatorContents.clear();
                calculatorContents.addInput("Hello World 2");

                break;
        }

        txtEquation.setText(calculatorContents.getUsrInput());
        if (calculatorContents.bDispResult()) {
            txtResult.setText(EQUALS + decimalFormat.format(calculatorContents.getCalcResult()));
        }
        else {
            txtResult.setText("");
        }
    }
}
