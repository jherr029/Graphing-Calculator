package teamteam.graphing_calculator;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

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

    private static final String DECIMAL = ".";
    private static final String PI = "π";

    private static final String EQUALS = "=";

    private static final String ERROR = "ERROR";

    private BasicCalculatorContents calculatorContents = BasicCalculatorContents.get();

    private DecimalFormat decimalFormat;

    private TextView txtEquation;
    private TextView txtResult;

    private BottomSheetBehavior mBottomSheetBehavior;

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
        Button btnPI = (Button) findViewById(R.id.btnPI);
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

        btnPI.setOnClickListener(this);
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
                mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
                mBottomSheetBehavior.setHideable(true);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_calculator);

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

        if (!calculatorContents.Connected()) {
            calculatorContents.connect();
        }

        SetLayout(getResources().getConfiguration());
    }

    @TargetApi(24)
    private void recallMemory() {
        ArrayList<MemoryValue> memoryValues = calculatorContents.recallMemory();
        memoryValues.sort(new Comparator<MemoryValue>() {
            @Override
            public int compare(MemoryValue o1, MemoryValue o2) {
                return (int) (o1.getTimestamp() - o2.getTimestamp());
            }
        });
        LinearLayout menu = (LinearLayout) findViewById(R.id.bottom_sheet_list);
        menu.removeAllViews();

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnClose = new Button(this);
        btnClose.setLayoutParams(btnParams);
        btnClose.setText("Close");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        menu.addView(btnClose);

        for (MemoryValue mv : memoryValues) {
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtParams.setMargins(12, 0, 12, 0);

            TextView tvEquation = new TextView(this);
            tvEquation.setText(mv.getEquation());
            tvEquation.setTextSize(18);
            tvEquation.setPadding(0, 10, 0, 0);
            tvEquation.setLayoutParams(txtParams);

            menu.addView(tvEquation);

            TextView tvResult = new TextView(this);
            tvResult.setText(decimalFormat.format(mv.getResult()));
            tvResult.setTextSize(32);
            tvResult.setPadding(0, 10, 0, 0);
            tvResult.setGravity(Gravity.RIGHT);
            tvResult.setClickable(true);
            tvResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView me = (TextView) v;
                    calculatorContents.addInput(me.getText().toString());
                    txtEquation.setText(calculatorContents.getUsrInput());
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            });
            tvResult.setLayoutParams(txtParams);

            menu.addView(tvResult);

            View divider = new View(this);
            int dividerHeight = (int) getResources().getDisplayMetrics().density * 1;

            divider.setBackgroundColor(Color.parseColor("#000000"));
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));

            menu.addView(divider);
        }
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnPI:
                calculatorContents.addInput(PI);
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
                calculatorContents.storeResult();
                break;
            case R.id.btnMC:
                calculatorContents.clearMemory();
                break;
            case R.id.btnMR:
                /* TODO add memory features */
                recallMemory();
                break;
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
        else if (calculatorContents.bDispError()) {
            txtResult.setText(ERROR);
        }
        else {
            txtResult.setText("");
        }
    }
}
