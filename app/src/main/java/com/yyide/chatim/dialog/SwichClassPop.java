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

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.SwichClassAdapter;
import com.yyide.chatim.adapter.SwichSchoolAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwichClassPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;

    OkHttpClient mOkHttpClient = new OkHttpClient();
    public SwichClassPop(Activity context) {
        this.context = context;
        init();
    }

    private void init() {


        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_class_pop, null);

         popupWindow= new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);


        FrameLayout layout = (FrameLayout)mView.findViewById(R.id.layout);

        FrameLayout bg = (FrameLayout)mView.findViewById(R.id.bg);
        ListView listview = (ListView)mView.findViewById(R.id.listview);
        SwichClassAdapter adapter=new SwichClassAdapter();

        listview.setAdapter(adapter);

        List<String> list=new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        adapter.notifyData(list);
        adapter.setIndex(1);


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.getContentView().setFocusable(true);
        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(popupWindow!=null && popupWindow.isShowing()){
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

                if (popupWindow!= null&&popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

            }
        });
        // 获取当前Activity的window
        Activity activity = (Activity) mView.getContext();
        if(activity!=null ){
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            mWindow= activity.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 0.7f;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }

        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //如果设置了背景变暗，那么在dissmiss的时候需要还原
                Log.e("TAG", "onDismiss==>: " );
                if(mWindow!=null){
                    WindowManager.LayoutParams params = mWindow.getAttributes();
                    params.alpha = 1.0f;
                    mWindow.setAttributes(params);
                }
                if(popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(mView,Gravity.NO_GRAVITY,0,0);



    }





}
