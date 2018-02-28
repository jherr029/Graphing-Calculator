package teamteam.graphing_calculator;

import android.app.Activity;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

/**
 * Created by cero on 2/25/18.
 */

public class MathKeyboard {
    private KeyboardView mKeyboardView;
    private Activity mHostActivity;

    private static final String tagKeyboard = "keyboard";

    private OnKeyboardActionListener mOnKeyboardActionListener =
            new OnKeyboardActionListener() {

        public final static int CodeDelete      = -5;
        public final static int CodeCancelt     = -3;
        public final static int CodePrev        = 55000;
        public final static int CodeAllLeft     = 55001;
        public final static int CodeLeft        = 55002;
        public final static int CodeRight       = 55003;
        public final static int CodeAllRight    = 55004;
        public final static int CodeNext        = 55005;
        public final static int CodeClear       = 55006;

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            Log.d(tagKeyboard, "onKey");

//          Problem is this place
            View focusCurrent = mHostActivity.getWindow().getCurrentFocus();

            if (focusCurrent != null) {
                Log.d(tagKeyboard,"focusCurrent is not null");
            }

            if (focusCurrent.getClass() != EditText.class) {

                Log.d(tagKeyboard,"" + focusCurrent.getClass());
                Log.d(tagKeyboard,"" + EditText.class);
                Log.d(tagKeyboard,"but this is not correct");
            }

//          TODO figure out why the value is null
//          What is wrong is that the class types dont match
//          however its not really an issue
//          TODO reactivating the keyobard after its been hidden is an issue
            if(focusCurrent == null || focusCurrent.getClass() != EditText.class) {
                Log.d(tagKeyboard,"something is null");
//                return;
            }

            Log.d(tagKeyboard,"has been pressed");

            EditText editText = (EditText) focusCurrent;
            Editable editable = editText.getText();

            int start = editText.getSelectionStart();

            if (primaryCode == CodeCancelt) {
                hideKeyboard();
            } else if (primaryCode == CodeDelete) {
                if (editable != null && start > 0)
                    editable.delete(start - 1, start);
            } else if (primaryCode == CodeClear) {
                if ( editable != null)
                    editable.clear();
            } else if (primaryCode == CodeLeft) {
                if (start > 0)
                    editText.setSelection(start - 1);
            } else if (primaryCode == CodeAllLeft) {
                editText.setSelection(0);
            } else if (primaryCode == CodeRight) {
                if (start < editText.length())
                    editText.setSelection(start + 1);
            } else if (primaryCode == CodeAllRight) {
                editText.setSelection(editText.length());
            } else if (primaryCode == CodePrev) {
                View focusNew = editText.focusSearch(View.FOCUS_BACKWARD);

                if (focusNew != null)
                    focusNew.requestFocus();

            } else if (primaryCode == CodeNext) {
                View focusNew = editText.focusSearch(View.FOCUS_FORWARD);

                if (focusNew != null)
                    focusCurrent.requestFocus();
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
        @Override
        public void onPress(int primaryCode) {
            Log.d(tagKeyboard,"onPress " + primaryCode);
        }

        @Override
        public void onRelease(int primaryCode) {

        }


        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    public MathKeyboard(Activity host, int viewId, int layoutId) {
        mHostActivity = host;

        Log.d(tagKeyboard, "KEYBOARD HAS BEEN CREATED");

        mKeyboardView = (KeyboardView) mHostActivity.findViewById(viewId);

        if (mHostActivity == null) {
           Log.d("NULL", "NULLLLLLLLL");
        } else {
            Log.d(tagKeyboard, "mHostActivity is not null");
        }

        if (layoutId == 0) {
           Log.d("NULL", "NULLLLLLLLL 00000000");
        }

        mKeyboardView.setKeyboard(new android.inputmethodservice.Keyboard(mHostActivity, layoutId));
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);

        mHostActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public boolean isKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    public void showKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);

        if (v != null) {
            ((InputMethodManager) mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void hideKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    public void registerEditText(int resid) {

        Log.d(tagKeyboard,"registerEditText");

        final EditText editText = (EditText) mHostActivity.findViewById(resid);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if (hasFocus)
                   showKeyboard(v);
               else
                   hideKeyboard();
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(tagKeyboard, "setOnTouchListener");

                EditText editText = (EditText) v;

                int inType = editText.getInputType();
                editText.setInputType(InputType.TYPE_NULL); editText.onTouchEvent(event);
                editText.setInputType(inType);

                return true;
            }
        });

        editText.setInputType(editText.getInputType() |
            TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    }
}


















