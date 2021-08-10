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


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwitchTableClassPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private List<SelectTableClassesRsp.DataBean> dataBeansList;
    private SelectClasses mSelectClasses;
    private ArrayWheelAdapter classesWheelAdapter;

    public void setSelectClasses(SelectClasses selectClasses) {
        this.mSelectClasses = selectClasses;
    }

    public interface SelectClasses {
        void OnSelectClassesListener(long id, String classesName);
    }

    public SwitchTableClassPop(Activity context, List<SelectTableClassesRsp.DataBean> dataBeansList) {
        this.context = context;
        this.dataBeansList = dataBeansList;
        init();
    }

    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bttom_table_class_pop, null);
        TextView confirm = mView.findViewById(R.id.confirm);
        TextView cancel = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        WheelView wheelView = mView.findViewById(R.id.departments);
        WheelView wheelViewClasses = mView.findViewById(R.id.tableClass);
        confirm.setOnClickListener(v -> {
            if (classesWheelAdapter.getData() != null && classesWheelAdapter.getData().size() >= wheelViewClasses.getCurrentItem()) {
                SelectTableClassesRsp.DataBean itemData = (SelectTableClassesRsp.DataBean) classesWheelAdapter.getData().get(wheelViewClasses.getCurrentItem());
                if (mSelectClasses != null && itemData != null) {
                    mSelectClasses.OnSelectClassesListener(itemData.getId(), itemData.getName());
                }
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
        wheelView.setCyclic(false);
        wheelView.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        wheelView.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        wheelViewClasses.setCyclic(false);
        wheelViewClasses.setTextColorCenter(context.getResources().getColor(R.color.text_212121));
        wheelViewClasses.setTextColorOut(context.getResources().getColor(R.color.text_666666));
        wheelViewClasses.setCurrentItem(-1);
        wheelView.setAdapter(new ArrayWheelAdapter(dataBeansList));
        wheelView.setCurrentItem(-1);
        wheelView.setOnItemSelectedListener(index -> {
            if (dataBeansList != null && dataBeansList.size() > 0 && dataBeansList.get(index) != null) {
                classesWheelAdapter = new ArrayWheelAdapter(dataBeansList.get(index).getList());
                wheelViewClasses.setAdapter(classesWheelAdapter);
            }
        });
        if (dataBeansList != null && dataBeansList.size() > 0 && dataBeansList.get(0) != null) {
            classesWheelAdapter = new ArrayWheelAdapter(dataBeansList.get(0).getList());
            wheelViewClasses.setAdapter(classesWheelAdapter);
        }
        wheelViewClasses.setOnItemSelectedListener(index -> {

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

    private void getListOption(List<SelectTableClassesRsp.DataBean> dataBeans) {
        if (dataBeans == null) return;
        for (SelectTableClassesRsp.DataBean item : dataBeans) {

        }
    }

}
