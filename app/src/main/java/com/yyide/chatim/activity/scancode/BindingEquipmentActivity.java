package com.yyide.chatim.activity.scancode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.ActivityBindingEquipmentBinding;
import com.yyide.chatim.model.ActivateRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.ClassBrandInfoRsp;
import com.yyide.chatim.presenter.scan.BindingEquipmentPresenter;
import com.yyide.chatim.view.scan.BindingEquipmentView;

/**
 * 扫码登录
 *
 * @author liu tao
 * @date 2021年8月5日18:07:05
 */
public class BindingEquipmentActivity extends BaseMvpActivity<BindingEquipmentPresenter> implements BindingEquipmentView {
    private static final String TAG = BindingEquipmentActivity.class.getSimpleName();
    private ActivityBindingEquipmentBinding binding;
    private String id;
    private String registrationCode;
    private String code;
    private String brandStatus;
    private String activateState;
    private String activateCode;
    private String bindStatus;//注册码绑定状态
    private String equipmentSerialNumber;
    private String bindingState;//人脸绑定状态

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
        final Intent intent = getIntent();
        equipmentSerialNumber = intent.getStringExtra("equipmentSerialNumber");
        brandStatus = intent.getStringExtra("brandStatus");
        code = intent.getStringExtra("code");

        binding.top.backLayout.setOnClickListener(v -> finish());
        binding.btnGetRegisterCode.setOnClickListener(v -> {
            if (TextUtils.isEmpty(registrationCode)) {
                binding.tvRegisterCode.setText("暂无可用注册码");
                binding.tvRegisterCode.setTextColor(getResources().getColor(R.color.black));
                binding.btnGetRegisterCode.setEnabled(false);
                binding.btnGetRegisterCode.setAlpha(0.5f);
                binding.btnEnter.setEnabled(false);
                binding.btnEnter.setAlpha(0.5f);
                return;
            }
            binding.tvRegisterCode.setText(registrationCode);
            binding.tvRegisterCode.setTextColor(getResources().getColor(R.color.black));
        });
        mvpPresenter.getRegistrationCodeByOffice(equipmentSerialNumber);
        //获取激活码
        binding.btnActiveCode.setOnClickListener(v -> {
            if ("2".equals(activateState)) {
                binding.tvActiveCode.setTextColor(getResources().getColor(R.color.black));
                binding.tvActiveCode.setText(R.string.face_recongize_disable_tip);
            } else if ("1".equals(activateState)) {
                binding.tvActiveCode.setText(activateCode);
                binding.tvActiveCode.setTextColor(getResources().getColor(R.color.black));
            } else {
                binding.tvActiveCode.setText(R.string.current_school_no_face_activate_tip);
                binding.tvActiveCode.setTextColor(getResources().getColor(R.color.black));
                binding.btnActiveCode.setEnabled(false);
                binding.btnActiveCode.setAlpha(0.5f);
            }
        });
        binding.btnEnter.setOnClickListener(v -> {
            //去绑定设备
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(registrationCode)) {
                ToastUtils.showShort(R.string.get_register_code_tip);
                return;
            }
            if (TextUtils.isEmpty(registrationCode) && TextUtils.isEmpty(activateCode)) {
                ToastUtils.showShort(R.string.get_activate_code_tip);
                return;
            }

            if ("1".equals(bindStatus)) {
                toConfirmLogin();
                return;
            }

            mvpPresenter.updateRegistrationCodeByCode(id, "1", equipmentSerialNumber, registrationCode);
        });
    }

    @Override
    protected BindingEquipmentPresenter createPresenter() {
        return new BindingEquipmentPresenter(this);
    }

    @Override
    public void showError() {

    }

    @Override
    public void findRegistrationCodeSuccess(ClassBrandInfoRsp classBrandInfoRsp) {
        Log.e(TAG, "findRegistrationCodeSuccess: " + classBrandInfoRsp.toString());
        if (classBrandInfoRsp.getCode() == 200) {
            final ClassBrandInfoRsp.DataBean data = classBrandInfoRsp.getData();
            if (data != null) {
                registrationCode = data.getRegistrationCode();
                bindStatus = data.getStatus();
                id = data.getId();
            }

        }
        mvpPresenter.findActivationCode(equipmentSerialNumber);
    }

    @Override
    public void findRegistrationCodeFail(String msg) {
        Log.e(TAG, "findRegistrationCodeFail: " + msg);
        mvpPresenter.findActivationCode(equipmentSerialNumber);
    }

    @Override
    public void updateRegistrationCodeSuccess(BaseRsp baseRsp) {
        Log.e(TAG, "updateRegistrationCodeSuccess: " + baseRsp.toString());
        if (baseRsp.getCode() == 200) {
            //绑定成功，进入选择
            toConfirmLogin();
        }
    }

    private void toConfirmLogin() {
        Intent intent = new Intent(this, ConfirmLoginActivity.class);
        intent.putExtra("brandStatus", brandStatus);
        intent.putExtra("code", code);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateRegistrationCodeFail(String msg) {
        Log.e(TAG, "updateRegistrationCodeFail: " + msg);
        ToastUtils.showShort(msg);
    }

    @Override
    public void findActivationCodeSuccess(ActivateRsp activateRsp) {
        Log.e(TAG, "findActivationCodeSuccess: " + activateRsp.toString());
        if (activateRsp.getCode() == 200) {
            final ActivateRsp.DataBean data = activateRsp.getData();
            if (data != null) {
                activateState = data.getActivateState();
                //激活状态（1：已启用，2：禁用）
                activateCode = data.getActivateCode();
                bindingState = data.getBangingState();
            }
        }

        if ("1".equals(bindStatus) && "1".equals(bindingState)) {
            toConfirmLogin();
            return;
        }

        binding.gpLayout.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(activateCode)) {
            binding.clActiveCode.setVisibility(View.GONE);
        } else {
            binding.clActiveCode.setVisibility("1".equals(bindingState) ? View.GONE : View.VISIBLE);
        }
        binding.clRegisterCode.setVisibility("1".equals(bindStatus) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void findActivationCodeFail(String msg) {
        Log.e(TAG, "findActivationCodeFail: " + msg);
        binding.gpLayout.setVisibility(View.VISIBLE);
        binding.clActiveCode.setVisibility(View.GONE);
        binding.clRegisterCode.setVisibility("1".equals(bindStatus) ? View.GONE : View.VISIBLE);
    }
}