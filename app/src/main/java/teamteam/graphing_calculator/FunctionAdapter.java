package teamteam.graphing_calculator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by makloooo on 2/25/18.
 */

public class FunctionAdapter extends BaseAdapter {

    private static final String TAG = "FunctionAdapter";

    private MainActivity mContext;
    private LayoutInflater mInflater;

    private class Input {
        String display = "";
        String complete = "";
        Input() {}
        Input(String function) { display = function; complete = function; }
        Input(String display, String complete) {
            this.display = display; this.complete = complete;
        }
    }
    // Pair<Input, Complete>
    private ArrayList<Input> mFunctionList; // Holds User Input Strings

    public RegexInterpreter mRegexInterpreter;

    // To listen for text input and graph updating.
    private class FunctionWatcher implements TextWatcher {
        private ImageView mErrorIcon;
        private ImageView mGraphIcon;
        private int mIndex;

        public FunctionWatcher(FrameLayout layout, int index) {
            this.mErrorIcon = (ImageView)layout.getChildAt(2);
            this.mGraphIcon = (ImageView)layout.getChildAt(3);
            this.mIndex = index;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String prevFunction = mFunctionList.get(mIndex).complete;
            if (s.toString().isEmpty()) {
                mContext.graph.remove_line(prevFunction);
                mErrorIcon.setVisibility(View.INVISIBLE);
                mGraphIcon.setVisibility(View.INVISIBLE);
            }
            else if (mRegexInterpreter.isValidFunction(s.toString())) {
                Paint prevPaint = mContext.graph.getColor(prevFunction);
                // graph the function, remove any error icons
                if (!prevFunction.isEmpty()) mContext.graph.remove_line(prevFunction);
                mFunctionList.get(mIndex).complete = s.toString();
                mContext.graph.add_line(s.toString(), prevPaint);
                mContext.graph.highlight(s.toString());

                mGraphIcon.setBackgroundColor(mContext.graph.getColor(s.toString()).getColor());
                mGraphIcon.setVisibility(View.VISIBLE);
                mErrorIcon.setVisibility(View.INVISIBLE);
            }
            else {
                mGraphIcon.setVisibility(View.INVISIBLE);
                mErrorIcon.setVisibility(View.VISIBLE);
            }
            mFunctionList.get(mIndex).display = s.toString(); // Update Input List
        }
    }

    FunctionAdapter(MainActivity activity) {
        mContext = activity;
        mFunctionList = new ArrayList<>();
        mFunctionList.add(new Input()); mFunctionList.add(new Input());
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRegexInterpreter = new RegexInterpreter();
    }

    @Override
    public int getCount() {
        return mFunctionList.size();
    }

    @Override
    public String getItem(int position) {
        return mFunctionList.get(position).display;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get view for row item
        final View functionView = mInflater.inflate(R.layout.function_list_item, parent, false);

        // Get Refs
        TextView functionIndex = functionView.findViewById(R.id.function_index);
        final EditText functionText = functionView.findViewById(R.id.func);
        ImageButton functionDelete = functionView.findViewById(R.id.function_delete);

        // Set function label
        functionIndex.setText(String.valueOf(position+1));

        // Create Text Listeners
        functionText.addTextChangedListener(
                new FunctionWatcher((FrameLayout)functionText.getParent(), position));
        functionText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(TAG,"Currently focused function: " + functionText.getText().toString());
                    mContext.graph.highlight(functionText.getText().toString());
                    if (position == mFunctionList.size()-1) {
                        functionView.setForeground(null); // Unfade this panel
                        mFunctionList.add(new Input()); // Add layout input
                    }
                }
            }
        });

        // If last panel, fade it
        if (position == mFunctionList.size()-1) {
            Drawable fade = mContext.getBaseContext()
                                    .getDrawable(R.drawable.rectangle_gradient_fade);
            functionView.setForeground(fade);
        }

        // set EditText to user's input
        functionText.setText(mFunctionList.get(position).display);

        // Set listeners and define delete panel action
        functionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an animation listener to set to Gone when done
                functionView.animate()
                        .translationX(functionView.getWidth())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                functionView.setVisibility(View.GONE);

                                mContext.graph.remove_line(mFunctionList.get(position).complete);
                                mFunctionList.remove(position);

                                FrameLayout layout;
                                if (position > 0) layout = (FrameLayout)parent.getChildAt(position-1);
                                else layout = (FrameLayout)parent.getChildAt(0);
                                EditText next = (EditText)layout.getChildAt(1);
                                next.requestFocus();
                                //next.setSelection(next.getText().length());

                                // Make sure always at least two panels in list view
                                while (mFunctionList.size() < 2) mFunctionList.add(new Input());
                                notifyDataSetChanged();
                            }
                });
            }
        });

        return functionView;
    }

    void changeGraphType(RegexInterpreter.GraphType graphType) {
        mRegexInterpreter.changeGraphType(graphType);
        mFunctionList.clear();
        while (mFunctionList.size() < 2) mFunctionList.add(new Input());
        notifyDataSetChanged();
    }

    void setFunctions(ArrayList<String> functions) {
        mFunctionList.clear();
        for (int i = 0; i < functions.size(); ++i) {
            mFunctionList.add(new Input(functions.get(i)));
        }
        while (mFunctionList.size() < 2) mFunctionList.add(new Input());
        notifyDataSetChanged();
    }

    ArrayList<String> getFunctions() {
        ArrayList<String> completeFunctions = new ArrayList<>();
        for (int i = 0; i < mFunctionList.size(); ++i) {
            completeFunctions.add(mFunctionList.get(i).complete);
        }
        return completeFunctions;
    }

}