package com.yyide.chatim.activity.scancode;

import android.os.Bundle;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.ActivityBindingEquipmentBinding;
import com.yyide.chatim.presenter.scan.BindingEquipmentPresenter;
import com.yyide.chatim.view.scan.BindingEquipmentView;

/**
 * 扫码登录
 * @author liu tao
 * @date 2021年8月5日18:07:05
 */
public class BindingEquipmentActivity extends BaseMvpActivity<BindingEquipmentPresenter> implements BindingEquipmentView {
    private static final String TAG = BindingEquipmentActivity.class.getSimpleName();
    private ActivityBindingEquipmentBinding binding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_binding_equipment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBindingEquipmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.top.title.setText(R.string.app_scan_code);
        binding.top.backLayout.setOnClickListener(v -> finish());
        binding.btnEnter.setOnClickListener(v -> jupm(this,ConfirmLoginActivity.class));

        //app注册码验证
        //http://192.168.3.133:8888/brand/class-brand-management/android/appRegistraCode/findRegistrationCodeByOffice

        //app注册码验证
        //http://192.168.3.133:8888/brand/class-brand-management/android/appRegistraCode/findRegistrationCodeByOffice

        //app注册码绑定
        //http://192.168.3.133:8888/brand/class-brand-management/android/appRegistraCode/updateRegistrationCodeByCode
    }

    @Override
    protected BindingEquipmentPresenter createPresenter() {
        return new BindingEquipmentPresenter(this);
    }

    @Override
    public void showError() {

    }
}