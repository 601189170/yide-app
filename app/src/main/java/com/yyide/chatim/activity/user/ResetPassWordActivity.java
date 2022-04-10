package com.yyide.chatim.activity.user;


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
    @BindView(R.id.oldPassword)
    EditText etOldPassword;
    @BindView(R.id.etConfirmPwd)
    EditText etConfirmPwd;
    @BindView(R.id.post_code)
    TextView postCode;
    @BindView(R.id.new_password)
    EditText etNewPassword;
    @BindView(R.id.eye)
    ImageView eye;
    @BindView(R.id.ivConfirmEye)
    ImageView ivConfirmEye;
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
//            if (!TextUtils.isEmpty(mobile)) {
//                oldPassword.setText(mobile);
//            }
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
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPwd = etConfirmPwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtils.showShort("请输入旧密码");
        } else if (TextUtils.isEmpty(newPassword)) {
            ToastUtils.showShort("请输入新密码");
        } else if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtils.showShort("请输入确认密码");
        } else if (newPassword.length() < 6) {
            ToastUtils.showShort("请输入6位以上密码");
        } else if (!confirmPwd.equals(newPassword)) {
            ToastUtils.showShort("新密码与确认密码有误");
        } else {
            mvpPresenter.updatePwd(oldPassword, newPassword, confirmPwd);
        }
    }

    @OnClick({R.id.tv_confirm, R.id.back_layout, R.id.post_code, R.id.eye, R.id.ivConfirmEye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.tv_confirm:
                submit();
                break;
            case R.id.post_code://获取验证码
//                String mobile = phone.getText().toString().trim();
//                if (TextUtils.isEmpty(mobile)) {
//                    ToastUtils.showShort("请输入手机号码");
//                } else {
//                    mvpPresenter.getSmsCode(mobile);
//                }
                break;
            case R.id.eye:
                if (!eye.isSelected()) {
                    eye.setSelected(true);
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    eye.setSelected(false);
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.ivConfirmEye:
                if (!ivConfirmEye.isSelected()) {
                    ivConfirmEye.setSelected(true);
                    etConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ivConfirmEye.setSelected(false);
                    etConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
