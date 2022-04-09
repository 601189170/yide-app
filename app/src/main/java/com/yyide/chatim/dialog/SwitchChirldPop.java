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
import com.yyide.chatim.model.getClassSubjectListRsp;
import com.yyide.chatim.model.selectParentStudent;
import com.yyide.chatim.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwitchChirldPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private SelectDateListener mSelectDateListener;
//    private TimeWheelAdapter hoursWheelAdapter = new TimeWheelAdapter(getHour(0), "时");
//    private TimeWheelAdapter minuteWheelAdapter = new TimeWheelAdapter(getMinute(-1), "分");
    private int hours;
    private int minute;
    private selectParentStudent date1;

    List<selectParentStudent> list;

    TimeWheelAdapter ljAdapter, classAdapter,subjectAdapter;

    public void setSelectClasses(SelectDateListener selectClasses) {
        this.mSelectDateListener = selectClasses;
    }

    public interface SelectDateListener {
        void onSelectDateListener(selectParentStudent bean);
    }

    public SwitchChirldPop(Activity context, List<selectParentStudent> list) {
        this.context = context;
        this.list=list;
        init();
    }
    public SwitchChirldPop(Activity context) {
        this.context = context;
        this.list=list;
        init();
    }
    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_top_chirld_pop, null);
        TextView confirm = mView.findViewById(R.id.confirm);
        TextView cancel = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        WheelView time = mView.findViewById(R.id.time);


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
        if (getData().size()>0){
            ljAdapter = new TimeWheelAdapter(getData());
            time.setAdapter(ljAdapter);
            date1 = (selectParentStudent) ljAdapter.stringList.get(0);
        }



        time.setOnItemSelectedListener(index -> {
            date1 = (selectParentStudent) ljAdapter.stringList.get(index);
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
                mSelectDateListener.onSelectDateListener(date1);
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
        List<selectParentStudent> stringList = new ArrayList<>();

        public int index;
        public TimeWheelAdapter(List<selectParentStudent> list) {
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

        public void setData(List<selectParentStudent> stringList){
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

    private List<selectParentStudent> getData() {
        List<selectParentStudent> stringList = new ArrayList<>();
        for (selectParentStudent bean : list) {
            stringList.add(bean);
        }
        return stringList;
    }


}
