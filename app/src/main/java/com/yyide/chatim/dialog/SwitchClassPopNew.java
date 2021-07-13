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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.SwichClassAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwitchClassPopNew extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private GetUserSchoolRsp.DataBean.FormBean classBean;

    public SwitchClassPopNew(Activity context, GetUserSchoolRsp.DataBean.FormBean classBean) {
        this.context = context;
        this.classBean = classBean;
        init();
    }

    private OnCheckCallBack mOnCheckCallBack;

    public void setOnCheckCallBack(OnCheckCallBack mOnCheckCallBack) {
        this.mOnCheckCallBack = mOnCheckCallBack;
    }

    public interface OnCheckCallBack {
        void onCheckCallBack(GetUserSchoolRsp.DataBean.FormBean classBean);
    }

    private void init() {
        int index = 0;
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_class_pop, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
        TextView tv_cancel = mView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        FrameLayout bg = mView.findViewById(R.id.bg);
        ListView listview = mView.findViewById(R.id.listview);
        SwichClassAdapter adapter = new SwichClassAdapter();
        listview.setAdapter(adapter);
        //保存班级ID用于切换班级业务逻辑使用
        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().form != null) {
            adapter.notifyData(SpData.getIdentityInfo().form);
            for (int i = 0; i < SpData.getIdentityInfo().form.size(); i++) {
                if (classBean != null
                        && classBean.classesId != null
                        && classBean.classesId.equals(SpData.getIdentityInfo().form.get(i).classesId)) {
                    index = i;
                    break;
                }
            }
        }
        adapter.setIndex(index);
        listview.setOnItemClickListener((parent, view, position, id) -> {
            adapter.setIndex(position);
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            if (mOnCheckCallBack != null) {
                mOnCheckCallBack.onCheckCallBack(adapter.getItem(position));
            }
//                ActivityUtils.finishAllActivities();
//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
        });

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
        bg.setOnClickListener(v -> {

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

}
