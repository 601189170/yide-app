package com.yyide.chatim_pro.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.UserActivity;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.utils.TakePicUtil;

import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2019/5/15.
 */

public class BottomHeadMenuPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private String title;
    OkHttpClient mOkHttpClient = new OkHttpClient();

    public BottomHeadMenuPop(Activity context) {
        this.context = context;
        init();
    }

    public BottomHeadMenuPop(Activity context, String title) {
        this.context = context;
        this.title = title;
        init();
    }

    private void init() {

        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_head_pop, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);

        FrameLayout bg = (FrameLayout) mView.findViewById(R.id.bg);
        TextView s1 = (TextView) mView.findViewById(R.id.s1);
        TextView s2 = (TextView) mView.findViewById(R.id.s2);
        TextView tvTitle = (TextView) mView.findViewById(R.id.tvTitle);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        s1.setOnClickListener(v -> {
            selectFromTake();
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        s2.setOnClickListener(v -> {
            selectFromGallery();
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        tvTitle.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.getContentView().setFocusable(true);
        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        // 获取当前Activity的window
        Activity activity = (Activity) mView.getContext();
        if (activity != null) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            mWindow = activity.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 0.5f;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }

        popupWindow.setOnDismissListener(() -> {
            //如果设置了背景变暗，那么在dissmiss的时候需要还原
            Log.e("TAG", "onDismiss==>: ");
            if (mWindow != null) {
                WindowManager.LayoutParams params = mWindow.getAttributes();
                params.alpha = 1.0f;
                mWindow.setAttributes(params);
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * 拍取照片不裁切
     */
    private void selectFromTake() {
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
        Disposable mDisposable = rxPermissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
            if (granted) {
                TakePicUtil.takePicture(context, true);
            } else {
                // 权限被拒绝
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage(R.string.permission_camera)
                        .setPositiveButton("开启", (dialog, which) -> {
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                            context.startActivity(localIntent);
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });
    }

    /**
     * 打开手机相册
     */
    private void selectFromGallery() {
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
        Disposable mDisposable = rxPermissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
            if (granted) {
                TakePicUtil.albumPhoto(context, true);
            } else {
                // 权限被拒绝
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage(R.string.permission_file)
                        .setPositiveButton("开启", (dialog, which) -> {
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                            context.startActivity(localIntent);
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });
    }

}
