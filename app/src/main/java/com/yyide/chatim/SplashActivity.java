package com.yyide.chatim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.utils.DemoLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int SPLASH_TIME = 1500;
    private View mFlashView;
    private UserInfo mUserInfo;
    public String loginName;
    public String passWord;
    OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mFlashView = findViewById(R.id.flash_view);
        mUserInfo = UserInfo.getInstance();
        Log.e(TAG, "mUserInfo: " + JSON.toJSONString(mUserInfo));
        initData();
        Log.e(TAG, "loginName: " + JSON.toJSONString(loginName));
        Log.e(TAG, "passWord: " + JSON.toJSONString(passWord));
        if (!TextUtils.isEmpty(loginName) && !TextUtils.isEmpty((passWord))) {
            Tologin(loginName, passWord);
        } else {
            startLogin();
        }
//        handleData();
    }

    void initData() {
        loginName = SPUtils.getInstance().getString(BaseConstant.LOGINNAME, null);
        passWord = SPUtils.getInstance().getString(BaseConstant.PASSWORD, null);
    }

    void Tologin(String username, String password) {
        RequestBody body = null;
        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().schoolId > 0) {
            int schoolId = SpData.getIdentityInfo().schoolId;
            if (schoolId > 0) {
                body = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .add("schoolId", schoolId + "")
                        .build();
            } else {
                body = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .build();
            }
        } else {
            body = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();
        }

        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/login")
                .post(body)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                startLogin();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "mOkHttpClient==>: " + data);
                LoginRsp bean = JSON.parseObject(data, LoginRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    //存储登录信息
                    SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
                    getUserSchool();
                } else {
                    startLogin();
                }
            }
        });
    }

    //获取学校信息
    void getUserSchool() {
        //请求组合创建
        Request request = new Request.Builder()
//                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/im/getUserSig")
                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/user/getUserSchoolByApp")
                .addHeader("Authorization", SpData.User().token)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getUserSigonFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "getUserSchool333==>: " + data);
                GetUserSchoolRsp rsp = JSON.parseObject(data, GetUserSchoolRsp.class);
                SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
                if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
                    if (rsp.data != null) {
                        SpData.setIdentityInfo(rsp);
                    } else {
                        ToastUtils.showShort(rsp.msg);
                    }
                    handleData();
                } else {
                    ToastUtils.showShort(rsp.msg);
                    startLogin();
                }
            }
        });
    }

    private void handleData() {
        if (mUserInfo != null && mUserInfo.isAutoLogin()) {
            loginIM();
        } else {
            mFlashView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startLogin();
                }
            }, SPLASH_TIME);
        }
    }

    private void loginIM() {
        TUIKit.login(mUserInfo.getUserId(), mUserInfo.getUserSig(), new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                        Log.e(TAG, "UserInfo: " + JSON.toJSONString(UserInfo.getInstance()));
//                        startMain();
                        startLogin();
                    }
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
                Log.e(TAG, "UserInfo==》: " + JSON.toJSONString(UserInfo.getInstance()));
                startMain();
            }
        });
    }

    private void startLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMain() {
//        OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(getIntent());
//        if (bean != null) {
//            OfflineMessageDispatcher.redirect(bean);
//            finish();
//            return;
//        }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
