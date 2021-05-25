package com.yyide.chatim.dialog;

import android.app.Activity;
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
import com.yyide.chatim.R;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.widget.ArrayWheelAdapter;

import java.util.List;

public class AttendancePop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private List<String> dataBeansList;
    private SelectClasses mSelectClasses;
    public void BottomSelectWindow(SelectClasses selectClasses) {
        this.mSelectClasses = selectClasses;
    }

    public interface SelectClasses {
        void OnSelectClassesListener(int id, String classesName);
    }

    public AttendancePop(Activity context, List<String> dataBeansList) {
        this.context = context;
        this.dataBeansList = dataBeansList;
        init();
    }

    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bttom_pop, null);
        TextView confirm = mView.findViewById(R.id.confirm);
        TextView cancel = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        WheelView wheelView = mView.findViewById(R.id.departments);
        confirm.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        wheelView.setCyclic(false);
        wheelView.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        wheelView.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public int getItemsCount() {
                return dataBeansList.size();
            }

            @Override
            public Object getItem(int index) {
                return dataBeansList.get(index);
            }

            @Override
            public int indexOf(Object o) {
                return dataBeansList.indexOf(o);
            }
        });
        wheelView.setCurrentItem(-1);
        wheelView.setOnItemSelectedListener(index -> {
            dataBeansList.get(index);
        });

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
