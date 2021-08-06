package com.yyide.chatim.activity.scancode;

import android.os.Bundle;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.ActivityBindingEquipmentBinding;
import com.yyide.chatim.databinding.ActivityConfirmLoginBinding;
import com.yyide.chatim.presenter.scan.BindingEquipmentPresenter;
import com.yyide.chatim.presenter.scan.ConfirmLoginPresenter;
import com.yyide.chatim.view.scan.BindingEquipmentView;
import com.yyide.chatim.view.scan.ConfirmLoginView;
/**
 * 扫码登录
 * @author liu tao
 * @date 2021年8月5日18:07:05
 */
public class ConfirmLoginActivity extends BaseMvpActivity<ConfirmLoginPresenter> implements ConfirmLoginView {
    private static final String TAG = ConfirmLoginActivity.class.getSimpleName();
    private ActivityConfirmLoginBinding binding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_confirm_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.top.title.setText(R.string.app_scan_code);
        binding.top.backLayout.setOnClickListener(v -> finish());
        binding.btnLogin.setOnClickListener(v -> jupm(this,ConfirmSuccessActivity.class));

        //获取班牌列表
        //https://api.uat.edu.1d1j.net/brand/class-brand-management/app/loginCheck/getClassBrand
        //登录
        //https://api.uat.edu.1d1j.net/brand/class-brand-management/app/loginCheck/scan/3c954c6c-ee86-4fc8-b018-936fb85defc7?userName=admin
    }

    @Override
    protected ConfirmLoginPresenter createPresenter() {
        return new ConfirmLoginPresenter(this);
    }

    @Override
    public void showError() {

    }
}