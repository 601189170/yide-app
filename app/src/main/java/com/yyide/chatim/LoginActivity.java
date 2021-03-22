package com.yyide.chatim;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.chat.info.UserInfo;
import com.yyide.chatim.chat.signature.GenerateTestUserSig;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.utils.Utils;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <p>
 * Demo的登录Activity
 * 用户名可以是任意非空字符，但是前提需要按照下面文档修改代码里的 SDKAPPID 与 PRIVATEKEY
 * https://github.com/tencentyun/TIMSDK/tree/master/Android
 * <p>
 */

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.user_edit)
    EditText userEdit;
    @BindView(R.id.password_edit)
    EditText passwordEdit;
    @BindView(R.id.type)
    CheckedTextView type;
    private Button mLoginView;
    private EditText mUserAccount;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    public String phone = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_for_dev_activity);
        ButterKnife.bind(this);

        mLoginView = findViewById(R.id.login_btn);
        // 用户名可以是任意非空字符，但是前提需要按照下面文档修改代码里的 SDKAPPID 与 PRIVATEKEY
        // https://github.com/tencentyun/TIMSDK/tree/master/Android
        mUserAccount = findViewById(R.id.login_user);
        mUserAccount.setText(UserInfo.getInstance().getUserId());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Utils.checkPermission(this);
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tologin(userEdit.getText().toString(), passwordEdit.getText().toString());
//                TologinBymobile(mUserAccount.getText().toString(),"15920012647");
            }
        });
        type.setText("验证码登录");
        type.setChecked(false);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.isChecked()){
                    type.setChecked(true);
                    type.setText("账号密码登录");
                }else {
                    type.setChecked(false);
                    type.setText("验证码登录");
                }

            }
        });
//        getcode("15920012647");
    }

    void initIm() {
        UserInfo.getInstance().setUserId(mUserAccount.getText().toString());
        // 获取userSig函数
        final String userSig = GenerateTestUserSig.genTestUserSig(mUserAccount.getText().toString());
        TUIKit.login(mUserAccount.getText().toString(), userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    }
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
                UserInfo.getInstance().setAutoLogin(true);
                UserInfo.getInstance().setUserSig(userSig);
                UserInfo.getInstance().setUserId(mUserAccount.getText().toString());
                Log.e(TAG, "UserInfo==》: " + UserInfo.getInstance().getUserId());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 系统请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.REQ_PERMISSION_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.toastLongMessage("未全部授权，部分功能可能无法使用！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void Tologin(String username, String password) {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
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
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "mOkHttpClientlogin==>: " + data);
                LoginRsp bean = JSON.parseObject(data, LoginRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    //存储登录信息
                    SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
                    SPUtils.getInstance().put(BaseConstant.LOGINNAME, username);
                    SPUtils.getInstance().put(BaseConstant.PASSWORD, password);
                    initIm();
                } else {
                    ToastUtils.showShort(bean.msg);
                }
            }
        });
    }

    //获取验证码
    void getcode(String phone) {
        RequestBody body = new FormBody.Builder()
                .add("phone", phone)
                .build();
        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.URL_IP + "/management/cloud-system/app/smsVerification")
                .post(body)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "mOkHttpClient==>: " + data);
                SmsVerificationRsp bean = JSON.parseObject(data, SmsVerificationRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    //存储登录信息
                    Log.e(TAG, "SmsVerificationRsp: " + JSON.toJSONString(bean));
                }
            }
        });
    }

    //验证码登入
    void TologinBymobile(String validateCode, String mobile) {
        RequestBody body = new FormBody.Builder()
                .add("validateCode", validateCode)
                .add("mobile", mobile)
                .build();
        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.URL_IP + "/management/cloud-system/authentication/mobile")
                .post(body)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "c==>: " + data);
                LoginRsp bean = JSON.parseObject(data, LoginRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    //存储登录信息
                    SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
                    SPUtils.getInstance().put(SpData.USERPHONE, phone);
                    initIm();
                } else {
                    ToastUtils.showShort(bean.msg);
                }
            }
        });
    }
}
