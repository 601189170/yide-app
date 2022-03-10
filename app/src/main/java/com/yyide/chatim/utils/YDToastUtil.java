package com.yyide.chatim.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.R;

public class YDToastUtil {
    private static Toast mToast;

    /**
     * 自定义Toast
     *
     * @param message
     */
    public static void showMessage(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        LayoutInflater inflater = LayoutInflater.from(BaseApplication.getInstance());
        View toast_view = inflater.inflate(R.layout.toast_message, null);
        TextView textView = toast_view.findViewById(R.id.tvMessage);
        textView.setText(message);
        mToast = new Toast(BaseApplication.getInstance());
        mToast.setGravity(Gravity.CENTER, 0, 0);
        //设置显示时间
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(toast_view);
        mToast.show();
    }
}
