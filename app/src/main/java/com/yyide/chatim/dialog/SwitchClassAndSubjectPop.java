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

import com.alibaba.fastjson.JSON;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.yyide.chatim.R;
import com.yyide.chatim.model.getClassSubjectListRsp;
import com.yyide.chatim.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwitchClassAndSubjectPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private SelectDateListener mSelectDateListener;
//    private TimeWheelAdapter hoursWheelAdapter = new TimeWheelAdapter(getHour(0), "时");
//    private TimeWheelAdapter minuteWheelAdapter = new TimeWheelAdapter(getMinute(-1), "分");
    private int hours;
    private int minute;
    private getClassSubjectListRsp date1;
    private getClassSubjectListRsp date2;
    private getClassSubjectListRsp date3;
    List<getClassSubjectListRsp> list;

    TimeWheelAdapter ljAdapter, classAdapter,subjectAdapter;

    public void setSelectClasses(SelectDateListener selectClasses) {
        this.mSelectDateListener = selectClasses;
    }

    public interface SelectDateListener {
        void onSelectDateListener(getClassSubjectListRsp bean1, getClassSubjectListRsp bean2,getClassSubjectListRsp bean3);
    }

    public SwitchClassAndSubjectPop(Activity context,List<getClassSubjectListRsp> list) {
        this.context = context;
        this.list=list;
        init();
    }
    public SwitchClassAndSubjectPop(Activity context) {
        this.context = context;
        this.list=list;
        init();
    }
    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_top_class_subject_pop, null);
        TextView confirm = mView.findViewById(R.id.confirm);
        TextView cancel = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        WheelView time = mView.findViewById(R.id.time);
        WheelView hour_time = mView.findViewById(R.id.hour_time);
        WheelView minute_time = mView.findViewById(R.id.minute_time);
        time.setLineSpacingMultiplier(3f);
        hour_time.setLineSpacingMultiplier(3f);
        minute_time.setLineSpacingMultiplier(3f);
//        time.setTextSize(14f);
//        hour_time.setTextSize(14f);
//        minute_time.setTextSize(14f);

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
        time.setTextColorCenter(context.getResources().getColor(R.color.text_333333));
        time.setTextColorOut(context.getResources().getColor(R.color.text_999999));
        ljAdapter = new TimeWheelAdapter(getljData());

        time.setAdapter(ljAdapter);


        time.setOnItemSelectedListener(index -> {
            classAdapter= new TimeWheelAdapter(getclassData(ljAdapter.stringList.get(index)));
            hour_time.setAdapter(classAdapter);
            date1 = (getClassSubjectListRsp) ljAdapter.stringList.get(index);
//            hour_time.setCurrentItem(0);
//            minute_time.setCurrentItem(0);
        });



        hour_time.setCyclic(false);
        hour_time.setTextColorCenter(context.getResources().getColor(R.color.text_333333));
        hour_time.setTextColorOut(context.getResources().getColor(R.color.text_999999));
        hour_time.setCurrentItem(0);
        hour_time.setOnItemSelectedListener(index -> {


            subjectAdapter = new TimeWheelAdapter(getSubjectData(classAdapter.stringList.get(index)));

            minute_time.setAdapter(subjectAdapter);

            date2 = (getClassSubjectListRsp) classAdapter.stringList.get(index);

//            minute_time.setCurrentItem(0);
        });

        minute_time.setCyclic(false);
        minute_time.setTextColorCenter(context.getResources().getColor(R.color.text_333333));
        minute_time.setTextColorOut(context.getResources().getColor(R.color.text_999999));


        minute_time.setOnItemSelectedListener(index -> {
            date3 = (getClassSubjectListRsp) subjectAdapter.stringList.get(index);
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

        confirm.setOnClickListener(v -> {
            if (mSelectDateListener != null) {
                mSelectDateListener.onSelectDateListener(date1,date2,date3);
            }
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
        List<getClassSubjectListRsp> stringList = new ArrayList<>();

        public int index;
        public TimeWheelAdapter(List<getClassSubjectListRsp> list) {
            this.stringList = list;
        }
        public TimeWheelAdapter() {

        }
        public int getItemValue(int index) {
            return Integer.parseInt(String.valueOf(stringList.get(index)));
        }

        @Override
        public int getItemsCount() {
            return stringList != null ? stringList.size() : 0;
        }

        @Override
        public String getItem(int index) {
            return stringList.get(index).name;
        }

        @Override
        public int indexOf(Object o) {
            return stringList.indexOf(o);
        }

        public void setData(List<getClassSubjectListRsp> stringList){
            this.stringList=stringList;
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

//    private List<String> getDate() {
//        List<String> stringList = new ArrayList<>();
//        stringList.add("今天");
//        stringList.add("明天");
//        return stringList;
//    }
    private List<getClassSubjectListRsp> getljData() {
        List<getClassSubjectListRsp> stringList = new ArrayList<>();
        for (getClassSubjectListRsp getClassSubjectListRsp : list) {
            stringList.add(getClassSubjectListRsp);
        }
        return stringList;
    }

    private List<getClassSubjectListRsp> getclassData(getClassSubjectListRsp bean) {
        List<getClassSubjectListRsp> stringList = new ArrayList<>();

        for (getClassSubjectListRsp child : bean.children) {
            stringList.add(child);
        }

        return stringList;
    }
    private List<getClassSubjectListRsp> getSubjectData(getClassSubjectListRsp bean) {
        List<getClassSubjectListRsp> stringList = new ArrayList<>();
        for (getClassSubjectListRsp child : bean.children) {
            stringList.add(child);
        }
        return stringList;
    }
//    private List<String> getHour(int index) {
//        Calendar calendar = Calendar.getInstance();
//        if (index > 0) {
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
//        }
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        List<String> stringList = new ArrayList<>();
//        for (int i = hour; i < 24; i++) {
//            if (i < 10) {
//                stringList.add("0" + hour++ + "");
//            } else {
//                stringList.add(hour++ + "");
//            }
//        }
//        return stringList;
//    }

//    private List<String> getMinute(int index) {
//        Calendar calendar = Calendar.getInstance();
//        if (index > 0) {
//            calendar.set(Calendar.MINUTE, 0);
//        }
//        int minute = calendar.get(Calendar.MINUTE);
//        List<String> stringList = new ArrayList<>();
//        for (int i = minute; i < 60; i++) {
//            if (i < 10) {
//                stringList.add("0" + minute++ + "");
//            } else {
//                stringList.add(minute++ + "");
//            }
//        }
//        return stringList;
//    }

}
