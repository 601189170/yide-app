package com.yyide.chatim.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.google.common.collect.EvictingQueue;
import com.yyide.chatim.R;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.widget.ArrayWheelAdapter;

import java.util.List;

public class AttendancePop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private int selectIndex;
    private SelectClasses mSelectClasses;
    private WheelView wheelView;
    private WheelAdapter wheelAdapter;

    public void setOnSelectListener(SelectClasses selectClasses) {
        this.mSelectClasses = selectClasses;
    }

    public void setCurrentItem(String name) {
        int index = 0;
        if (wheelAdapter != null && !TextUtils.isEmpty(name)) {
            for (int i = 0; i < wheelAdapter.getItemsCount(); i++) {
                if (name.equals(wheelAdapter.getItem(i))) {
                    index = i;
                    break;
                }
            }
        }

        if (index < wheelAdapter.getItemsCount()) {
            if (wheelView != null) {
                wheelView.setCurrentItem(index);
            }
        }
    }

    public interface SelectClasses {
        void OnSelectClassesListener(int index);
    }

    public AttendancePop(Activity context, WheelAdapter wheelAdapter, String leftText) {
        this.context = context;
        this.wheelAdapter = wheelAdapter;
        init(wheelAdapter, leftText);
    }

    private void init(WheelAdapter wheelAdapter, String leftText) {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bttom_pop, null);
        TextView confirm = mView.findViewById(R.id.confirm);
        TextView leftDesc = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        wheelView = mView.findViewById(R.id.departments);
        confirm.setOnClickListener(v -> {
            if (mSelectClasses != null) {
                mSelectClasses.OnSelectClassesListener(wheelView.getCurrentItem());
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        leftDesc.setText(leftText);
        wheelView.setCyclic(false);
        wheelView.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        wheelView.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        wheelView.setAdapter(wheelAdapter);
        wheelView.setCurrentItem(-1);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.getContentView().setFocusable(true);
        popupWindow.getContentView().setFocusableInTouchMode(true);
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
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);
    }

}
