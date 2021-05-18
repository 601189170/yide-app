package com.yyide.chatim.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.widget.LoadingView;

public class LoadingTools {
    private LoadingTools mInstance;
    private Dialog mLoadingDialog;

    public LoadingTools getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LoadingTools.class) {
                if (mInstance == null) {
                    mInstance = new LoadingTools(context);
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public LoadingTools(Context context) {
        mLoadingDialog = new Dialog(context, R.style.dialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        LoadingView loadingView = view.findViewById(R.id.loadingView);
        TextView text = view.findViewById(R.id.text);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(true);
        mLoadingDialog.setContentView(view);
        loadingView.start();
        mLoadingDialog.setOnDismissListener(dialog -> loadingView.stop());
        Window window = mLoadingDialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(R.style.dialogWindowAnim);
        }
    }

    public void showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void closeLoading() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog.cancel();
        }
    }
}
