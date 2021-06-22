package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Intent;
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
import com.yyide.chatim.utils.DateUtils;

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
    private SelectDateListener mSelectDateListener;
    private TimeWheelAdapter hoursWheelAdapter = new TimeWheelAdapter(getHour(0), "时");
    private TimeWheelAdapter minuteWheelAdapter = new TimeWheelAdapter(getMinute(-1), "分");
    private int hours;
    private int minute;
    private String date;

    public void setSelectClasses(SelectDateListener selectClasses) {
        this.mSelectDateListener = selectClasses;
    }

    public interface SelectDateListener {
        void onSelectDateListener(String date);
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
            if (mSelectDateListener != null) {
                mSelectDateListener.onSelectDateListener(getDateTime(date, hours, minute));
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
        cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        time.setCyclic(false);
        time.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        time.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        TimeWheelAdapter timeWheelAdapter = new TimeWheelAdapter(getDate(), "");
        time.setAdapter(timeWheelAdapter);
        time.setCurrentItem(0);
        time.setOnItemSelectedListener(index -> {
            date = (String) timeWheelAdapter.getItem(index);
            this.hoursWheelAdapter = null;
            minuteWheelAdapter = null;
            this.hoursWheelAdapter = new TimeWheelAdapter(getHour(index), "时");
            hour_time.setAdapter(this.hoursWheelAdapter);
            minuteWheelAdapter = new TimeWheelAdapter(getMinute(index), "分");
            minute_time.setAdapter(minuteWheelAdapter);
            hours = 0;
            minute = 0;
            hour_time.setCurrentItem(0);
            minute_time.setCurrentItem(0);
        });

        hour_time.setCyclic(false);
        hour_time.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        hour_time.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        hour_time.setAdapter(this.hoursWheelAdapter);
        hour_time.setCurrentItem(0);
        hours = hoursWheelAdapter.getItemValue(hour_time.getCurrentItem());
        hour_time.setOnItemSelectedListener(index -> {
            hours = hoursWheelAdapter.getItemValue(index);
            minuteWheelAdapter = null;
            if ("明天".equals(date)) {
                index++;
            }
            minuteWheelAdapter = new TimeWheelAdapter(getMinute(index), "分");
            minute_time.setAdapter(minuteWheelAdapter);
        });

        minute_time.setCyclic(false);
        minute_time.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        minute_time.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        minute_time.setAdapter(minuteWheelAdapter);
        minute_time.setCurrentItem(0);
        minute = minuteWheelAdapter.getItemValue(hour_time.getCurrentItem());
        minute_time.setOnItemSelectedListener(index -> {
            minute = (Integer) minuteWheelAdapter.getItemValue(index);
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

    public class TimeWheelAdapter implements WheelAdapter {
        private List<String> stringList = new ArrayList<>();
        private String tip;

        public TimeWheelAdapter(List<String> list, String tip) {
            this.stringList = list;
            this.tip = tip;
        }

        public int getItemValue(int index) {
            return Integer.parseInt(stringList.get(index));
        }

        @Override
        public int getItemsCount() {
            return stringList != null ? stringList.size() : 0;
        }

        @Override
        public Object getItem(int index) {
            return stringList.get(index) + tip;
        }

        @Override
        public int indexOf(Object o) {
            return stringList.indexOf(o);
        }
    }

    private String getDateTime(String date, int hours, int minute) {
        Log.d("SwitchNoticeTimePop", "hours-->>>:" + hours + "   minute-->>>：" + minute);
        Calendar c = Calendar.getInstance();
        if ("明天".equals(date)) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        c.clear(Calendar.HOUR_OF_DAY);
        c.clear(Calendar.MINUTE);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 00);
        Log.d("SwitchNoticeTimePop", "getDateTime-->>>:" + DateUtils.switchTime(c.getTime()));
        return DateUtils.switchTime(c.getTime());
    }

    private List<String> getDate() {
        List<String> stringList = new ArrayList<>();
        stringList.add("今天");
        stringList.add("明天");
        return stringList;
    }

    private List<String> getHour(int index) {
        Calendar calendar = Calendar.getInstance();
        if (index > 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        List<String> stringList = new ArrayList<>();
        for (int i = hour; i < 24; i++) {
            if (i < 10) {
                stringList.add("0" + hour++ + "");
            } else {
                stringList.add(hour++ + "");
            }
        }
        return stringList;
    }

    private List<String> getMinute(int index) {
        Calendar calendar = Calendar.getInstance();
        if (index > 0) {
            calendar.set(Calendar.MINUTE, 0);
        }
        int minute = calendar.get(Calendar.MINUTE);
        List<String> stringList = new ArrayList<>();
        for (int i = minute; i < 60; i++) {
            if (i < 10) {
                stringList.add("0" + minute++ + "");
            } else {
                stringList.add(minute++ + "");
            }
        }
        return stringList;
    }

}
