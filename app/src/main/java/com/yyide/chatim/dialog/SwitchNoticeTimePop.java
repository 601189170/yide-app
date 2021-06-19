package com.yyide.chatim.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.InputDevice;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwitchNoticeTimePop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private SelectClasses mSelectClasses;
    private TimeWheelAdapter timeWheelAdapter = new TimeWheelAdapter(getHour(0));
    private TimeWheelAdapter timeWheelAdapter1 = new TimeWheelAdapter(getMinute(0));

    public void setSelectClasses(SelectClasses selectClasses) {
        this.mSelectClasses = selectClasses;
    }

    public interface SelectClasses {
        void OnSelectClassesListener(long id, String classesName);
    }

    public SwitchNoticeTimePop(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bttom_notice_time_pop, null);
        TextView confirm = mView.findViewById(R.id.confirm);
        TextView cancel = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        WheelView time = mView.findViewById(R.id.time);
        WheelView hour_time = mView.findViewById(R.id.hour_time);
        WheelView minute_time = mView.findViewById(R.id.minute_time);
        confirm.setOnClickListener(v -> {

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

        time.setCyclic(false);
        time.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        time.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        time.setAdapter(new TimeWheelAdapter(getDate()));
        time.setCurrentItem(-1);
        time.setOnItemSelectedListener(index -> {
            timeWheelAdapter = new TimeWheelAdapter(getHour(index));
            hour_time.setAdapter(timeWheelAdapter);
        });

        hour_time.setCyclic(false);
        hour_time.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        hour_time.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        hour_time.setCurrentItem(-1);
        hour_time.setAdapter(timeWheelAdapter);
        hour_time.setOnItemSelectedListener(index -> {
            timeWheelAdapter1 = new TimeWheelAdapter(getMinute(index));
            minute_time.setAdapter(timeWheelAdapter1);
        });

        minute_time.setCyclic(false);
        minute_time.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        minute_time.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        minute_time.setCurrentItem(-1);
        minute_time.setAdapter(timeWheelAdapter1);
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

    public class TimeWheelAdapter implements WheelAdapter {
        private List<String> stringList = new ArrayList<>();

        public TimeWheelAdapter(List<String> list) {
            this.stringList = list;
        }

        @Override
        public int getItemsCount() {
            return stringList != null ? stringList.size() : 0;
        }

        @Override
        public Object getItem(int index) {
            return stringList.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return stringList.indexOf(o);
        }
    };

    private List<String> getDate() {
        List<String> stringList = new ArrayList<>();
        stringList.add("今天");
        stringList.add("明天");
        return stringList;
    }

    private List<String> getHour(int index) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        List<String> stringList = new ArrayList<>();
        for (int i = hour; i < 24; i++) {
            stringList.add(hour++ + "时");
        }
        return stringList;
    }

    private List<String> getMinute(int index) {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        List<String> stringList = new ArrayList<>();
        for (int i = minute; i < 60; i++) {
            stringList.add(minute++ + "分");
        }
        return stringList;
    }

}
