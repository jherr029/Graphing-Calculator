package teamteam.graphing_calculator;

import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by makloooo on 3/7/18.
 */

class BuiltInFunctionHandler {
    private final static String TAG = "BuiltInFunctionHandler";

    private MainActivity mContext;
    private ArrayList<String> mFunctionBuffer;

    private LinearLayout mGraphToolView;
    private LinearLayout mCoefficientEditView;

    class Coefficient {
        LinearLayout layout;
        EditText editText;
        char coefficient;
        Coefficient(char coefficient) {
            this.coefficient = coefficient;
            switch (coefficient) {
                case 'a':
                    layout = mContext.findViewById(R.id.coefficient_a);
                    editText = mContext.findViewById(R.id.coefficient_a_value);
                    break;
                case 'b':
                    layout = mContext.findViewById(R.id.coefficient_b);
                    editText = mContext.findViewById(R.id.coefficient_b_value);
                    break;
                case 'c':
                    layout = mContext.findViewById(R.id.coefficient_c);
                    editText = mContext.findViewById(R.id.coefficient_c_value);
                    break;
                case 'd':
                    layout = mContext.findViewById(R.id.coefficient_d);
                    editText = mContext.findViewById(R.id.coefficient_d_value);
                    break;
                default:
                    Log.d(TAG, "Invalid Coefficient Extracted");
            }
        }

        void configure(String function) {
            Pattern pattern = Pattern.compile(coefficient+"([^a-z]|$)");
            Matcher matcher = pattern.matcher(function);
            if (!matcher.find()) layout.setVisibility(View.GONE);
            else {
                editText.setText("");
                layout.setVisibility(View.VISIBLE);
            }
        }
    }

    private Coefficient a, b, c, d;

    BuiltInFunctionHandler(MainActivity context) {
        mContext = context;
        mFunctionBuffer = new ArrayList<>();
        a = new Coefficient('a');
        b = new Coefficient('b');
        c = new Coefficient('c');
        d = new Coefficient('d');

        mGraphToolView = mContext.mGraphToolView;
        mCoefficientEditView = mContext.findViewById(R.id.edit_coefficients_popout);
        mCoefficientEditView.setVisibility(View.GONE);
    }

    private boolean substituteCoefficient(Coefficient k, int index) {
        if (k.layout.getVisibility() == View.GONE) return true;

        StringBuilder stringBuilder = new StringBuilder(mFunctionBuffer.get(index));
        String value = k.editText.getText().toString();
        Log.d(TAG,"String to Modify: " + stringBuilder.toString());
        Log.d(TAG,"EditText to Insert: " + value);

        // Is this a number or not?
        if (!value.matches("-?([0-9]+([.][0-9]+)?)")) {
            Log.d(TAG, "Invalid Coefficients entered.");
            mContext.DebugSnackbar("Invalid Coefficients entered.");
            return false;
        }

        // tan, cos ruin simple indexAt solution
        Pattern pattern = Pattern.compile(k.coefficient+"([^a-z]|$)");
        Matcher matcher = pattern.matcher(stringBuilder.toString());
        if (!matcher.find()) return false;
        int offset = matcher.start();
        stringBuilder.replace(offset, offset+1, value);
        Log.d(TAG,"Modified String: " + stringBuilder.toString());

        mFunctionBuffer.set(index, stringBuilder.toString());

        return true;
    }

    private void configureAndShowDropdown() {
        // Edit the mFunctionBuffer for coefficients
        TextView functionTextView = mContext.findViewById(R.id.edited_built_in_function);

        // TODO - compensate for multiple functions somehow
        String functionString = mFunctionBuffer.get(0);
        functionTextView.setText(functionString);

        a.configure(functionString);
        b.configure(functionString);
        c.configure(functionString);
        d.configure(functionString);

        // Just plot it if it's a standard function
        if (a.layout.getVisibility() == View.GONE &&
                b.layout.getVisibility() == View.GONE &&
                c.layout.getVisibility() == View.GONE &&
                d.layout.getVisibility() == View.GONE) {
            plotFunctions();
            return;
        }

        if (mGraphToolView.getVisibility() == View.VISIBLE) {
            mGraphToolView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out_anim));
            mGraphToolView.setVisibility(View.GONE);
        }
        mCoefficientEditView.setVisibility(View.VISIBLE);
        mCoefficientEditView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in_anim));
    }

    boolean setFunctions(int resId) {
        // Will probably kill run-time a bit
        mFunctionBuffer.clear();
        switch (resId) {
            case R.id.example_point_slope_form:
                mFunctionBuffer.add("a*x + b");
                break;
            case R.id.example_parabolic_curve:
                mFunctionBuffer.add("a*x^2 + b*x + c");
                break;
            case R.id.example_cubic_curve:
                mFunctionBuffer.add("a*x^3 + b*x^2 + c*x + d");
                break;
            case R.id.example_sin_wave:
                mFunctionBuffer.add("a*sin(b*x+c)");
                break;
            case R.id.example_cos_wave:
                mFunctionBuffer.add("a*cos(b*x+c)");
                break;
            case R.id.example_sin_function:
                mFunctionBuffer.add("a*sin(b*θ+c)");
                break;
            case R.id.example_cos_function:
                mFunctionBuffer.add("a*cos(b*θ+c)");
                break;
            case R.id.example_logarithmic_curve:
                mFunctionBuffer.add("log(a*x)");
                break;
            case R.id.example_natural_log:
                mFunctionBuffer.add("ln(a*x)");
                break;
            case R.id.example_exponential:
                mFunctionBuffer.add("x^a");
                break;
            default:
                mContext.DebugSnackbar("This feature doesn't exist yet!");
                return false;
        }
        changeGraph();
        configureAndShowDropdown();
        return true;
    }

    void plotFunctions() {
        for (int i = 0; i < mFunctionBuffer.size(); ++i) {
            // Code police will kill me for this
            if (!substituteCoefficient(a, i)) return;
            if (!substituteCoefficient(b, i)) return;
            if (!substituteCoefficient(c, i)) return;
            if (!substituteCoefficient(d, i)) return;
        }
        mContext.mFunctionAdapter.setFunctions(mFunctionBuffer);
        mContext.graph.setFunctions(mFunctionBuffer);
        mCoefficientEditView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out_anim));
        mCoefficientEditView.setVisibility(View.GONE);
    }

    void closeWindow() {
        if (mCoefficientEditView.getVisibility() == View.VISIBLE) {
            mCoefficientEditView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out_anim));
            mCoefficientEditView.setVisibility(View.GONE);
        }
    }

    void changeGraph() {
        if (mFunctionBuffer.get(0).indexOf('x') == -1) {
            mContext.onClick(mContext.findViewById(R.id.switch_to_polar));
        }
        else mContext.onClick(mContext.findViewById(R.id.switch_to_cartesian));
    }
}
