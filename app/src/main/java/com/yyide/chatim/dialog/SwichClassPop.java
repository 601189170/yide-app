package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.MainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.SwichClassAdapter;
import com.yyide.chatim.adapter.SwichSchoolAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.model.SelectUserSchoolRsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwichClassPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;

    public SwichClassPop(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        int index = 0;
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_class_pop, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);

        FrameLayout layout = (FrameLayout) mView.findViewById(R.id.layout);

        FrameLayout bg = (FrameLayout) mView.findViewById(R.id.bg);
        ListView listview = (ListView) mView.findViewById(R.id.listview);
        SwichClassAdapter adapter = new SwichClassAdapter();
        listview.setAdapter(adapter);
        //保存班级ID用于切换班级业务逻辑使用
        if (SpData.Schoolinfo() != null && SpData.Schoolinfo().data != null && SpData.Schoolinfo().data.size() > 0 && SpData.Schoolinfo().data.get(0).form != null) {
            adapter.notifyData(SpData.Schoolinfo().data.get(0).form);
            for (int i = 0; i < SpData.Schoolinfo().data.get(0).form.size(); i++) {
                if (SpData.getClassInfo() != null && SpData.Schoolinfo().data.get(0).form.get(i).classesId.equals(SpData.getClassInfo().classesId)) {
                    index = i;
                    break;
                }
            }
        }
        adapter.setIndex(index);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setIndex(position);
                SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(SpData.Schoolinfo().data.get(0).form.get(position)));
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
//                ActivityUtils.finishAllActivities();
//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.getContentView().setFocusable(true);
        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

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

        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
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
            }
        });
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);

    }

}
