package teamteam.graphing_calculator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by makloooo on 2/25/18.
 */

public class FunctionAdapter extends BaseAdapter {

    private MainActivity mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mFunctionList;

    private RegexInterpreter mRegexInterpreter;

    // To listen for text input and graph updating.
    private class FunctionWatcher implements TextWatcher {
        private EditText mEditText;
        private ImageView mErrorIcon;

        private String prevFunction;
        public FunctionWatcher(FrameLayout layout) {
            this.mEditText = (EditText)layout.getChildAt(1);
            this.mErrorIcon = (ImageView)layout.getChildAt(2);
            prevFunction = "";
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().isEmpty()) mErrorIcon.setVisibility(View.INVISIBLE);
            else if (mRegexInterpreter.isValidFunction(s.toString())) {
                // graph the function, remove any error icons
                // Log.d(TAG, "prevFunction: " + prevFunction);
                // Log.d(TAG, "newFunction: " + s.toString());
                if (!prevFunction.isEmpty()) mContext.graph.remove_line(prevFunction);
                prevFunction = mEditText.getText().toString();
                mContext.graph.add_line(s.toString());
                mErrorIcon.setVisibility(View.INVISIBLE);
            }
            else {
                // dont graph or remove function, display error icon
                mErrorIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    FunctionAdapter(MainActivity activity, ArrayList<String> functions) {
        mContext = activity;
        mFunctionList = functions;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRegexInterpreter = new RegexInterpreter();
    }

    @Override
    public int getCount() {
        return mFunctionList.size();
    }

    @Override
    public String getItem(int position) {
        return mFunctionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View functionView = mInflater.inflate(R.layout.function_list_item, parent, false);

        TextView functionIndex = functionView.findViewById(R.id.function_index);
        EditText functionText = functionView.findViewById(R.id.func);

        final boolean end_function = (position == mFunctionList.size()-1);

        functionIndex.setText(String.valueOf(position+1));
        functionText.addTextChangedListener(new FunctionWatcher((FrameLayout)functionText.getParent()));
        functionText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && end_function) {
                    mFunctionList.add("");
                }
            }
        });
        functionText.setText(mFunctionList.get(position));

        return functionView;
    }
}