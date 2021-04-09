package com.yyide.chatim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.activity.ResetPassWordActivity;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.chat.info.UserInfo;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.getUserSigRsp;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.utils.Utils;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.user_edit)
    EditText userEdit;
    @BindView(R.id.password_edit)
    EditText passwordEdit;
    @BindView(R.id.type)
    CheckedTextView type;
    @BindView(R.id.eye)
    ImageView eye;
    @BindView(R.id.ll_sms)
    LinearLayout ll_sms;
    @BindView(R.id.ll_pwd)
    LinearLayout ll_pwd;
    @BindView(R.id.post_code)
    TextView postCode;
    @BindView(R.id.code)
    EditText vCode;

    private TimeCount time;
    private boolean isPwd;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    public String phone = "";

    private AlphaAnimation alphaAniShow, alphaAniHide;

    @Override
    public int getContentViewID() {
        return R.layout.login_for_dev_activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Utils.checkPermission(this);
//        getcode("15920012647");
//        if (AppUtils.isAppDebug()) {
//            userEdit.setText("13522222222");
//            passwordEdit.setText("222222");
//        }
        String username = SPUtils.getInstance().getString(BaseConstant.LOGINNAME);
        String password = SPUtils.getInstance().getString(BaseConstant.PASSWORD);
        userEdit.setText(TextUtils.isEmpty(username) ? "" : username);
        passwordEdit.setText(TextUtils.isEmpty(password) ? "" : password);
        time = new TimeCount(60000, 1000);
        alphaAnimation();
    }

    //透明度动画
    private void alphaAnimation() {
        //显示
        alphaAniShow = new AlphaAnimation(0, 1);//百分比透明度，从0%到100%显示
        alphaAniShow.setDuration(800);//一秒

        //隐藏
        alphaAniHide = new AlphaAnimation(1, 0);
        alphaAniHide.setDuration(800);
    }

    @OnClick({R.id.forgot, R.id.tv_login, R.id.eye, R.id.type, R.id.post_code})
    void click(View view) {
        switch (view.getId()) {
            case R.id.forgot:
                Intent intent = new Intent(this, ResetPassWordActivity.class);
                intent.putExtra("isForgot", true);
                startActivity(intent);
                break;
            case R.id.tv_login:

                login();
                break;
            case R.id.eye:
                if (!eye.isSelected()) {
                    eye.setSelected(true);
                    passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    eye.setSelected(false);
                    passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.post_code:
                String mobile = userEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.showShort("请输入手机号码");
                } else {
                    getcode(mobile);
                }
                break;
            case R.id.type:
                isValidateCode();
                break;
        }
    }

    private void isValidateCode() {
        if (!isPwd) {
            isPwd = true;
            type.setText("账号密码登录");
            ll_pwd.startAnimation(alphaAniHide);
            //这个地方为什么要做动画的监听呢，因为隐藏和显示不一样，
            //必须在动画结束之后再隐藏你的控件，这样才不会显得很突兀
            alphaAniHide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ll_pwd.setVisibility(View.GONE);
                    passwordEdit.setText("");
                    ll_sms.startAnimation(alphaAniShow);
                    ll_sms.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            isPwd = false;
            type.setText("验证码登录");
            ll_sms.startAnimation(alphaAniHide);
            //这个地方为什么要做动画的监听呢，因为隐藏和显示不一样，
            //必须在动画结束之后再隐藏你的控件，这样才不会显得很突兀
            alphaAniHide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ll_sms.setVisibility(View.GONE);
                    ll_pwd.startAnimation(alphaAniShow);
                    ll_pwd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void login() {
        String validateCode = vCode.getText().toString().trim();
        String mobile = userEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        if (ll_sms.isShown()) {//处理登录逻辑
            if (TextUtils.isEmpty(mobile)) {
                ToastUtils.showShort("请输入手机号码");
            } else if (TextUtils.isEmpty(validateCode)) {
                ToastUtils.showShort("请输入验证码");
            } else {
                TologinBymobile(validateCode, mobile);
            }
        } else {
            if (TextUtils.isEmpty(mobile)) {
                ToastUtils.showShort("请输入手机号码");
            } else if (TextUtils.isEmpty(password)) {
                ToastUtils.showShort("请输入密码");
            } else {
                Tologin(userEdit.getText().toString(), passwordEdit.getText().toString());
            }
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            postCode.setText("获取验证码");
            postCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            postCode.setClickable(false);//防止重复点击
            postCode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();
        }
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
        showProgressDialog();
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
                dismissProgressDialog();
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
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    getUserSig();
                } else {
                    ToastUtils.showShort(bean.message);
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
        showProgressDialog();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissProgressDialog();
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dismissProgressDialog();
                String data = response.body().string();
                Log.e(TAG, "mOkHttpClient==>: " + data);
                SmsVerificationRsp bean = JSON.parseObject(data, SmsVerificationRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    //存储登录信息
                    ToastUtils.showShort("验证码已发送");
                    time.start();
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
        showProgressDialog();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissProgressDialog();
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
                    getUserSig();
                } else {
                    ToastUtils.showShort(bean.message);
                }
            }
        });
    }

    //计算 UserSig
    void getUserSig() {
        RequestBody body = new FormBody.Builder()
                .build();
        //请求组合创建
        Request request = new Request.Builder()
//                .url(BaseConstant.URL_IP + "/management/cloud-system/im/getUserSig")
                .url(BaseConstant.URL_IP + "/management/cloud-system/im/getUserSig")
//                .url("http://192.168.3.120:8010"+"/cloud-system/im/getUserSig")
                .addHeader("Authorization", SpData.User().token)
                .post(body)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissProgressDialog();
                Log.e(TAG, "getUserSigonFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "getUserSig==>: " + data);
                getUserSigRsp bean = JSON.parseObject(data, getUserSigRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    SPUtils.getInstance().put(SpData.USERSIG, bean.data);
                    getUserSchool();
                } else {
                    ToastUtils.showShort(bean.msg);
                }
            }
        });
    }

    //获取学校信息
    void getUserSchool() {
        //请求组合创建
        Request request = new Request.Builder()
//                .url(BaseConstant.URL_IP + "/management/cloud-system/im/getUserSig")
                .url(BaseConstant.URL_IP + "/management/cloud-system/user/getUserSchoolByApp")
                .addHeader("Authorization", SpData.User().token)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissProgressDialog();
                Log.e(TAG, "getUserSigonFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "getUserSchool333==>: " + data);
                GetUserSchoolRsp rsp = JSON.parseObject(data, GetUserSchoolRsp.class);
                SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
                if (rsp.data != null) {
                    if (rsp.data.size() > 0) {
                        for (int i = 0; i < rsp.data.size(); i++) {
                            if (rsp.data.get(i).isCurrentUser) {
//                                SPUtils.getInstance().put(SpData.USERID, rsp.data.get(i).userId);
//                                SPUtils.getInstance().put(SpData.SCHOOLID, rsp.data.get(i).schoolId);
                                SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(rsp.data.get(i)));
                                if (rsp.data.get(i) != null && rsp.data.get(i).form != null && rsp.data.get(i).form.size() > 0) {
                                    SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(rsp.data.get(i).form.get(0)));
                                }
                                initIm(SpData.getIdentityInfo().userId, SpData.UserSig());
                            }
                        }
                    }
                }
            }
        });
    }

    void initIm(int userid, String userSig) {
        TUIKit.login(String.valueOf(userid), userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                dismissProgressDialog();
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    }
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
                dismissProgressDialog();
                UserInfo.getInstance().setAutoLogin(true);
                UserInfo.getInstance().setUserSig(userSig);
                UserInfo.getInstance().setUserId(String.valueOf(userid));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功");
            }
        });
    }
}
