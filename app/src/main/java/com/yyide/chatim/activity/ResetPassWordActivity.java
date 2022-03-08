package com.yyide.chatim.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.presenter.ResetPasswordPresenter;
import com.yyide.chatim.view.ResetPasswordView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ResetPassWordActivity extends BaseMvpActivity<ResetPasswordPresenter> implements ResetPasswordView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.post_code)
    TextView postCode;
    @BindView(R.id.new_password)
    EditText newPassword;
    @BindView(R.id.eye)
    ImageView eye;

    private TimeCount time;

    @Override
    public int getContentViewID() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isForgot = getIntent().getBooleanExtra("isForgot", false);
        String mobile = getIntent().getStringExtra("phone");
        if (isForgot) {
            title.setText("忘记密码");
            if (!TextUtils.isEmpty(mobile)) {
                phone.setText(mobile);
            }
        } else {
            title.setText("修改密码");
        }
        time = new TimeCount(120000, 1000);
    }

    @Override
    protected ResetPasswordPresenter createPresenter() {
        return new ResetPasswordPresenter(this);
    }

    private void submit() {
        String mobile = phone.getText().toString().trim();
        String sms = code.getText().toString().trim();
        String newPwd = newPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShort("请输入手机号码");
        } else if (TextUtils.isEmpty(sms)) {
            ToastUtils.showShort("请输入验证码");
        } else if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.showShort("请输入新密码");
        } else {
            mvpPresenter.updatePwd(mobile, sms, newPwd);
        }
    }

    @OnClick({R.id.tv_confirm, R.id.back_layout, R.id.post_code, R.id.eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.tv_confirm:
                submit();
                break;
            case R.id.post_code://获取验证码
                String mobile = phone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.showShort("请输入手机号码");
                } else {
                    mvpPresenter.getSmsCode(mobile);
                }
                break;
            case R.id.eye:
                if (!eye.isSelected()) {
                    eye.setSelected(true);
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    eye.setSelected(false);
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
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
    public void updateSuccess(String msg) {
        ToastUtils.showShort(msg);
        finish();
    }

    @Override
    public void updateFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void getSmsSuccess(String msg) {
        ToastUtils.showShort(msg);
        time.start();
    }

    @Override
    public void getSmsFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();
        }
    }
}
