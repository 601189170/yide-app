package com.yyide.chatim.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomInputDialog extends Dialog {

    public CustomInputDialog(@NonNull Context context) {
        super(context);
    }

    public CustomInputDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomInputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        View view = getCurrentFocus();
        if (view instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
        super.dismiss();
    }
}