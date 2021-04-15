package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
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

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.utils.TakePicUtil;

import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2019/5/15.
 */

public class BottomHeadMenuPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;

    OkHttpClient mOkHttpClient = new OkHttpClient();

    public BottomHeadMenuPop(Activity context) {
        this.context = context;
        init();
    }

    private void init() {


        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_head_pop, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);


        FrameLayout layout = (FrameLayout) mView.findViewById(R.id.layout);

        FrameLayout bg = (FrameLayout) mView.findViewById(R.id.bg);
        TextView s1 = (TextView) mView.findViewById(R.id.s1);
        TextView s2 = (TextView) mView.findViewById(R.id.s2);
        TextView s3 = (TextView) mView.findViewById(R.id.s3);
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromTake();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromGallery();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
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
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

            }
        });
        // 获取当前Activity的window
        Activity activity = (Activity) mView.getContext();
        if (activity != null) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            mWindow = activity.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 0.7f;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }

        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
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
            }
        });
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);


    }


    /**
     * 拍取照片不裁切
     */
    private void selectFromTake() {
//        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//用来打开相机的Intent
//        if (takePhotoIntent.resolveActivity(context.getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
//            context.startActivityForResult(takePhotoIntent, BaseConstant.REQ_CODE);//启动相机
//        }
//        TakePicUtil.takePicture(context);
        TakePicUtil.takePicture(context, true);

    }


    private void selectFromGallery() {
//        // TODO Auto-generatedmethod stub
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);//Pick an item fromthe data
//        intent.setType("image/*");//从所有图片中进行选择
//        context.startActivityForResult(intent, BaseConstant.SELECT_ORIGINAL_PIC);
        TakePicUtil.albumPhoto(context, true);

//        Intent intent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            intent = new Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        } else {
//            intent = new Intent(
//                    Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//        }
//        context.startActivityForResult(intent, BaseConstant.SELECT_ORIGINAL_PIC);
    }

}
