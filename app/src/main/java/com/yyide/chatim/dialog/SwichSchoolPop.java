package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
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
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.MainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.SwichSchoolAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.chat.info.UserInfo;
import com.yyide.chatim.chat.signature.GenerateTestUserSig;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.model.SelectUserSchoolRsp;
import com.yyide.chatim.utils.DemoLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwichSchoolPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;

    OkHttpClient mOkHttpClient = new OkHttpClient();

    public SwichSchoolPop(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_school_class_pop, null);
        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
        FrameLayout layout = mView.findViewById(R.id.layout);
        FrameLayout bg = mView.findViewById(R.id.bg);
        ListView listview = mView.findViewById(R.id.listview);
        SwichSchoolAdapter adapter = new SwichSchoolAdapter();
        listview.setAdapter(adapter);
        if (SpData.Schoolinfo() != null && SpData.Schoolinfo().data != null) {
            adapter.notifyData(SpData.Schoolinfo().data);
        }

        listview.setOnItemClickListener((parent, view, position, id) -> {
            adapter.setIndex(position);
            selectUserSchool(adapter.getItem(position));
        });

//        adapter.setIndex(1);
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

    void selectUserSchool(GetUserSchoolRsp.DataBean school) {
        SchoolRsp rsp = new SchoolRsp();
        rsp.schoolId = school.schoolId;
        rsp.schoolName = school.schoolName;
        RequestBody requestBody = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(rsp));
        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.URL_IP + "/management/cloud-system/user/selectUserSchool")
                .addHeader("Authorization", SpData.User().token)
                .post(requestBody)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(school));
//                SPUtils.getInstance().put(SpData.USERID, school.userId);
                String data = response.body().string();
                Log.e("TAG", "mOkHttpClient==>: " + data);
                SelectUserSchoolRsp bean = JSON.parseObject(data, SelectUserSchoolRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    Tologin(bean.data.username, bean.data.password, String.valueOf(school.schoolId));
                }
                context.runOnUiThread(() -> {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                });
//                ActivityUtils.finishAllActivities();
//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
            }
        });
    }

    void Tologin(String username, String password, String schoolId) {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("schoolId", schoolId)
                .build();
        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.URL_IP + "/management/cloud-system/login")
                .post(body)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e("TAG", "mOkHttpClient==>: " + data);
                LoginRsp bean = JSON.parseObject(data, LoginRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    //存储登录信息
                    SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
                    SPUtils.getInstance().put(BaseConstant.LOGINNAME, username);
                    SPUtils.getInstance().put(BaseConstant.PASSWORD, password);
                    SPUtils.getInstance().put(SpData.SCHOOLID, schoolId);

                    initIm(username);
                } else {
                    ToastUtils.showShort(bean.msg);
                }
            }
        });
    }

    void initIm(String mUser) {
        UserInfo.getInstance().setUserId(mUser);
        // 获取userSig函数
        final String userSig = GenerateTestUserSig.genTestUserSig(mUser);
        TUIKit.login(mUser, userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    }
                });
                DemoLog.i("TAG", "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {

                UserInfo.getInstance().setAutoLogin(true);
                UserInfo.getInstance().setUserSig(userSig);
                UserInfo.getInstance().setUserId(mUser);
                Log.e("TAG", "切换成功UserInfo==》: " + UserInfo.getInstance().getUserId());
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

                ActivityUtils.finishAllActivities();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            }
        });
    }
}
