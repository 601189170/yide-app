package com.yyide.chatim.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yyide.chatim.R;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/9 15:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/9 15:18
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DialogUtil {
    public static void showAlertDialog(Context context, String title, String hintText, String leftText, String rightText, OnClickListener onClickListener) {
        new MaterialAlertDialogBuilder(
                context,
                R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle(title)
                .setMessage(hintText)
                .setNegativeButton(leftText, (dialog, which) -> {
                    onClickListener.onCancel(null);
                })
                .setPositiveButton(rightText, (dialog, which) -> {
                    onClickListener.onEnsure(null);
                })
                .show();
    }
    public static Dialog hintDialog(Context context, String title, String hintText, String leftText, String rightText, OnClickListener onClickListener) {
        FullScreenDialog tipDialog = new FullScreenDialog(context, R.style.MyDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_hint4, null);
        LinearLayout llhintDialog = view.findViewById(R.id.ll_hint_dialog);
        TextView titleView = view.findViewById(R.id.tv_title);
        titleView.setText(title);
        TextView titleHintText = view.findViewById(R.id.tv_hintText);
        titleHintText.setText(hintText);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        if (!TextUtils.isEmpty(leftText)) {
            tvCancel.setText(leftText);
        }
        tvCancel.setOnClickListener(v -> {
            onClickListener.onCancel(v);
            tipDialog.dismiss();
        });

        TextView tvEnsure = view.findViewById(R.id.tv_ensure);
        if (!TextUtils.isEmpty(rightText)) {
            tvEnsure.setText(rightText);
        }
        tvEnsure.setOnClickListener(v -> {
            onClickListener.onEnsure(view);
            tipDialog.dismiss();
        });

        tipDialog.setContentView(view);
        tipDialog.setCancelable(false);
        tipDialog.show();
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        //重新设置布局高度，可以理解为设置dialog高度
        ViewGroup.LayoutParams params = llhintDialog.getLayoutParams();
        params.height = (int) (screenHeight * 0.3);
        llhintDialog.setLayoutParams(params);
        //设置dialog宽度
        Window window = tipDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setLayout((int) (screenWidth * 0.8), attributes.height);
        //设置dialog背景为透明色
        window.setBackgroundDrawableResource(R.color.transparent);
        return tipDialog;
    }

    public interface OnClickListener {
        void onCancel(View view);

        void onEnsure(View view);
    }
}
