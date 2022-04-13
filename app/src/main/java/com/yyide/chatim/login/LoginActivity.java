package com.yyide.chatim.login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.BuildConfig;
import com.yyide.chatim.NewMainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.user.RegisterActivity;
import com.yyide.chatim.activity.user.ResetPassWordActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.base.MMKVConstant;
import com.yyide.chatim.database.ScheduleDaoUtil;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginAccountBean;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.model.UserSigRsp;
import com.yyide.chatim.presenter.LoginPresenter;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.utils.Utils;
import com.yyide.chatim.view.LoginView;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * <p>
 * Demo的登录Activity
 * 用户名可以是任意非空字符，但是前提需要按照下面文档修改代码里的 SDKAPPID 与 PRIVATEKEY
 * https://github.com/tencentyun/TIMSDK/tree/master/Android
 * <p>
 */

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginView {

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
    @BindView(R.id.del)
    ImageView del;
    @BindView(R.id.forgot)
    TextView forgot;
    private TimeCount time;
    private boolean isPwd;
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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Utils.checkPermission(this);
        //将系统导航栏背景色设置为应用主题色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }
        String username = SPUtils.getInstance().getString(BaseConstant.LOGINNAME);
        String password = SPUtils.getInstance().getString(BaseConstant.PASSWORD);
        initEdit();
        userEdit.setText(TextUtils.isEmpty(username) ? "" : username);
        passwordEdit.setText(TextUtils.isEmpty(password) ? "" : password);
        time = new TimeCount(120000, 1000);
        alphaAnimation();
        mvpPresenter.accountSwitch();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    private void initEdit() {
        userEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (!TextUtils.isEmpty(s1)) {
                    del.setVisibility(View.VISIBLE);
                } else {
                    del.setVisibility(View.GONE);
                }
            }
        });
    }

    //透明度动画
    private void alphaAnimation() {
        //显示
        alphaAniShow = new AlphaAnimation(0, 1);//百分比透明度，从0%到100%显示
        alphaAniShow.setDuration(500);//一秒

        //隐藏
        alphaAniHide = new AlphaAnimation(1, 0);
        alphaAniHide.setDuration(500);
    }

    private int debugOrRelease = 0;

    @OnClick({R.id.forgot, R.id.btn_login, R.id.eye, R.id.type, R.id.post_code, R.id.del, R.id.logo})
    void click(View view) {
        switch (view.getId()) {
            case R.id.logo:
                if (debugOrRelease == 5) {
                    debugOrRelease = 0;
                    BaseConstant.API_SERVER_URL = BaseConstant.API_SERVER_URL.equals(BaseConstant.API_SERVER_URL_RELEASE) ? BaseConstant.API_SERVER_URL_UAT : BaseConstant.API_SERVER_URL_RELEASE;
                }
                debugOrRelease++;
                break;
            case R.id.forgot:
                if (!isForget) {
                    String userName = userEdit.getText().toString().trim();
                    Intent intent = new Intent(this, ResetPassWordActivity.class);
                    if (!TextUtils.isEmpty(userName) && RegexUtils.isMobileExact(userName)) {
                        intent.putExtra("phone", userName);
                    }
                    intent.putExtra("isForgot", true);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.btn_login:
                //startActivity(new Intent(this, StatisticsActivity.class));
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
                passwordEdit.setSelection(passwordEdit.length());
                break;
            case R.id.post_code:
                String mobile = userEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.showShort("请输入手机号码");
                } else {
                    getCode(mobile);
                }
                break;
            case R.id.type:
                isValidateCode();
                break;
            case R.id.del:
                userEdit.setText("");
                break;
        }
    }

    private void isValidateCode() {
        if (!isPwd) {
            isPwd = true;
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
                    type.setText("账号密码登录");
                    ll_sms.startAnimation(alphaAniShow);
                    ll_sms.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            isPwd = false;
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
                    type.setText("验证码登录");
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
                tologinBymobile(validateCode, mobile);
            }
        } else {
            if (TextUtils.isEmpty(mobile)) {
                ToastUtils.showShort("请输入手机号码");
            } else if (TextUtils.isEmpty(password)) {
                ToastUtils.showShort("请输入密码");
            } else {
                toLogin(userEdit.getText().toString(), passwordEdit.getText().toString());
            }
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void getLoginSuccess(LoginRsp bean) {
//        if (bean.code == BaseConstant.REQUEST_SUCCES2) {
//            //存储登录信息
//            SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
//            getUserSchool();
//        } else {
//            hideLoading();
//            ToastUtils.showShort(bean.msg);
//        }
    }

    @Override
    public void loginMobileData(LoginRsp bean) {
//        if (bean.code == BaseConstant.REQUEST_SUCCES2) {
//            //存储登录信息
//            SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
//            SPUtils.getInstance().put(SpData.USERPHONE, phone);
//            //getUserSig();
//            getUserSchool();
//        } else {
//            hideLoading();
//            ToastUtils.showShort(bean.message);
//        }
    }


    @Override
    public void getCode(SmsVerificationRsp bean) {
        if (bean.code == BaseConstant.REQUEST_SUCCESS) {
            //存储登录信息
            ToastUtils.showShort("验证码已发送");
            time.start();
        } else {
            ToastUtils.showShort(bean.msg);
        }
    }

    @Override
    public void getUserSign(UserSigRsp bean) {
        if (bean.getCode() == BaseConstant.REQUEST_SUCCESS) {
            MMKV.defaultMMKV().encode(MMKVConstant.IM_DATA,bean.getData());
            initIm(SpData.getUserId(), bean.getData().getUserSig());
        } else {
            hideLoading();
            ToastUtils.showShort(bean.getMessage());
        }
    }

    @Override
    public void getFail(String msg) {
        hideLoading();
        Log.e(TAG, "onFailure: " + msg);
        if (BuildConfig.DEBUG) {
            ToastUtils.showShort(msg);
        }
    }

    @Override
    public void getUserSchool(GetUserSchoolRsp rsp) {
        SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
        ScheduleDaoUtil.INSTANCE.clearAll();
        if (rsp.code == BaseConstant.REQUEST_SUCCESS) {
            //登陆成功保存账号密码
            SPUtils.getInstance().put(BaseConstant.LOGINNAME, userEdit.getText().toString());
            SPUtils.getInstance().put(BaseConstant.PASSWORD, passwordEdit.getText().toString());
            SpData.setIdentityInfo(rsp);
            hideLoading();
            startActivity(new Intent(LoginActivity.this, NewMainActivity.class));
            //getUserSig();
        } else {
            hideLoading();
            ToastUtils.showShort(rsp.msg);
        }
    }

    private boolean isForget = false;

    @Override
    public void getAccountSwitch(LoginAccountBean model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCESS) {//1开启 0关闭
            if (model.getData() != null && "1".equals(model.getData().getValue())) {
                forgot.setText(model.getData().getName());
                isForget = true;
            } else {
                isForget = false;
                forgot.setText(R.string.forget);
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
        if (requestCode == Utils.REQ_PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.toastLongMessage("未全部授权，部分功能可能无法使用！");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void toLogin(String username, String password) {
        RequestBody body = new FormBody.Builder()
                .add("client_id", "yide-cloud")
                .add("grant_type", "password")
                .add("client_secret", "yide1234567")
                .add("version", "2")
                .add("username", username)
                .add("password", password)
                .build();
        mvpPresenter.Login(body);
    }

    //获取验证码
    void getCode(String phone) {
        mvpPresenter.getCode(phone);
    }

    //验证码登入
    void tologinBymobile(String validateCode, String mobile) {
        mvpPresenter.loginMobile(mobile, validateCode);
    }

    //获取学校信息
    private void getUserSchool() {
        mvpPresenter.getUserSchool();
    }

    private void getUserSig() {
        mvpPresenter.getUserSign();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initIm(String userId, String userSig) {
        TUIKit.login(String.valueOf(userId), userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(() -> {
                    //YDToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    SPUtils.getInstance().put(BaseConstant.LOGINNAME, userEdit.getText().toString());
                    SPUtils.getInstance().put(BaseConstant.PASSWORD, passwordEdit.getText().toString());
                    UserInfo.getInstance().setAutoLogin(false);
                    UserInfo.getInstance().setUserSig(userSig);
                    UserInfo.getInstance().setUserId(String.valueOf(userId));
                    startActivity(new Intent(LoginActivity.this, NewMainActivity.class));
                    finish();
                    Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功");
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
                hideLoading();
            }

            @Override
            public void onSuccess(Object data) {
                SPUtils.getInstance().put(BaseConstant.LOGINNAME, userEdit.getText().toString());
                SPUtils.getInstance().put(BaseConstant.PASSWORD, passwordEdit.getText().toString());
                UserInfo.getInstance().setAutoLogin(true);
                UserInfo.getInstance().setUserSig(userSig);
                UserInfo.getInstance().setUserId(String.valueOf(userId));
                startActivity(new Intent(LoginActivity.this, NewMainActivity.class));
                hideLoading();
                Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功");
                finish();
            }
        });
    }
}
