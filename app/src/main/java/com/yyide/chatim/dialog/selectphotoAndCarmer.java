package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.yyide.chatim.R;
import com.yyide.chatim.model.PostWorkBean;
import com.yyide.chatim.model.getClassList;
import com.yyide.chatim.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2019/5/15.
 */

public class selectphotoAndCarmer extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private SelectDateListener mSelectDateListener;
//    private TimeWheelAdapter hoursWheelAdapter = new TimeWheelAdapter(getHour(0), "时");
//    private TimeWheelAdapter minuteWheelAdapter = new TimeWheelAdapter(getMinute(-1), "分");
    private int hours;
    private int minute;




    public void setJK(SelectDateListener selectClasses) {
        this.mSelectDateListener = selectClasses;
    }

    public interface SelectDateListener {

        void onphoto();

        void oncarmer();
    }

    public selectphotoAndCarmer(Activity context, List<getClassList> list) {
        this.context = context;

        init();
    }
    public selectphotoAndCarmer(Activity context) {
        this.context = context;
        init();
    }
    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_top_carmer, null);

        ConstraintLayout bg = mView.findViewById(R.id.bg);
        ImageView close = mView.findViewById(R.id.close);
        TextView carmer = mView.findViewById(R.id.carmer);
        TextView photos = mView.findViewById(R.id.photos);



        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        close.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });


        carmer.setOnClickListener(v -> {
            if (mSelectDateListener!=null){
                mSelectDateListener.oncarmer();
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        photos.setOnClickListener(v -> {
            if (mSelectDateListener!=null){
                mSelectDateListener.onphoto();
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setBackgroundDrawable(null);
//        popupWindow.getContentView().setFocusable(true);
//        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.getContentView().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            }
            return false;
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
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);

    }

}
