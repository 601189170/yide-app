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
import com.yyide.chatim.model.PostWorkBean;
import com.yyide.chatim.model.SubjectBean;
import com.yyide.chatim.model.getClassList;
import com.yyide.chatim.model.getClassList;
import com.yyide.chatim.model.selectBean;
import com.yyide.chatim.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2019/5/15.
 */

public class postClassDataPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private SelectDateListener mSelectDateListener;
//    private TimeWheelAdapter hoursWheelAdapter = new TimeWheelAdapter(getHour(0), "时");
//    private TimeWheelAdapter minuteWheelAdapter = new TimeWheelAdapter(getMinute(-1), "分");
    private int hours;
    private int minute;
    private getClassList date1;
    private getClassList.Children date2;

    List<getClassList> list;

    TimeWheelAdapter  classAdapter;
    TimeWheelAdapter2  subjectAdatper;

    public void setJK(SelectDateListener selectClasses) {
        this.mSelectDateListener = selectClasses;
    }

    public interface SelectDateListener {
        void onSelectDateListener(PostWorkBean selectBean);
    }

    public postClassDataPop(Activity context, List<getClassList> list) {
        this.context = context;
        this.list=list;
        init();
    }
    public postClassDataPop(Activity context) {
        this.context = context;
        this.list=list;
        init();
    }
    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_top_pop_class, null);
        TextView confirm = mView.findViewById(R.id.confirm);
        TextView cancel = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        WheelView time1 = mView.findViewById(R.id.time);
        WheelView time2 = mView.findViewById(R.id.hour_time);


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

        time1.setCyclic(false);
        time1.setTextColorCenter(context.getResources().getColor(R.color.text_333333));
        time1.setTextColorOut(context.getResources().getColor(R.color.text_999999));
        if (getData().size()>0){
            classAdapter = new TimeWheelAdapter(getData());
            time1.setAdapter(classAdapter);
            date1 = (getClassList) classAdapter.stringList.get(0);
        }
        time1.setOnItemSelectedListener(index -> {
            date1 = (getClassList) classAdapter.stringList.get(index);
            subjectAdatper = new TimeWheelAdapter2(getsubjectData(classAdapter.stringList.get(index).getChildren()));
            time2.setAdapter(subjectAdatper);
            date2 = (getClassList.Children) subjectAdatper.stringList.get(0);
        });


        time2.setCyclic(false);
        time2.setTextColorCenter(context.getResources().getColor(R.color.text_333333));
        time2.setTextColorOut(context.getResources().getColor(R.color.text_999999));
        if (getData().size()>0){
//            subjectAdatper = new TimeWheelAdapter(getData());
            subjectAdatper = new TimeWheelAdapter2(getsubjectData(classAdapter.stringList.get(0).getChildren()));
            time2.setAdapter(subjectAdatper);
            date2 = (getClassList.Children) subjectAdatper.stringList.get(0);
        }



        time2.setOnItemSelectedListener(index -> {
            date2 = (getClassList.Children) subjectAdatper.stringList.get(index);
            Log.e("TAG", "setOnItemSelectedListener: "+ JSON.toJSONString(date2));
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
                PostWorkBean bean=new PostWorkBean();
                bean.ljname=date1.getName();
                bean.classId=date2.getId();
                bean.className=date2.getName();
                Log.e("TAG", "bean: "+ JSON.toJSONString(bean));

                mSelectDateListener.onSelectDateListener(bean);
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
        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);

    }

    public class TimeWheelAdapter implements WheelAdapter {
        List<getClassList> stringList = new ArrayList<>();

        public int index;
        public TimeWheelAdapter(List<getClassList> list) {
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
            return stringList.get(index).getName();
        }

        @Override
        public int indexOf(Object o) {
            return stringList.indexOf(o);
        }

        public void setData(List<getClassList> stringList){
            this.stringList=stringList;
        }
    }
    public class TimeWheelAdapter2 implements WheelAdapter {
        List<getClassList.Children> stringList = new ArrayList<>();

        public int index;
        public TimeWheelAdapter2(List<getClassList.Children> list) {
            this.stringList = list;
        }
        public TimeWheelAdapter2() {

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
            return stringList.get(index).getName();
        }

        @Override
        public int indexOf(Object o) {
            return stringList.indexOf(o);
        }

        public void setData(List<getClassList.Children> stringList){
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

    private List<getClassList> getData() {
        List<getClassList> stringList = new ArrayList<>();
        for (getClassList bean : list) {
            stringList.add(bean);
        }
        return stringList;
    }
    private List<getClassList.Children> getsubjectData(List<getClassList.Children> listdata) {
        List<getClassList.Children> stringList = new ArrayList<>();
        for (getClassList.Children listdatum : listdata) {
            stringList.add(listdatum);
        }

        return stringList;
    }

}
