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
        final String bindStatus = intent.getStringExtra("bindStatus");
        final String equipmentSerialNumber = intent.getStringExtra("equipmentSerialNumber");
        brandStatus = intent.getStringExtra("brandStatus");
        code = intent.getStringExtra("code");
        binding.clRegisterCode.setVisibility("0".equals(bindStatus) ? View.GONE : View.VISIBLE);

        binding.top.backLayout.setOnClickListener(v -> finish());
        binding.btnGetRegisterCode.setOnClickListener(v -> {
            mvpPresenter.getRegistrationCodeByOffice();
        });
        //获取激活码
        binding.btnActiveCode.setOnClickListener(v -> {
            mvpPresenter.findActivationCode(equipmentSerialNumber);
        });
        binding.btnEnter.setOnClickListener(v -> {
            if ("0".equals(bindStatus)) {
                Intent intent1 = new Intent(this, ConfirmLoginActivity.class);
                intent1.putExtra("brandStatus", brandStatus);
                intent1.putExtra("code", code);
                startActivity(intent1);
                finish();
                return;
            }
            //去绑定设备
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(registrationCode)) {
                ToastUtils.showShort(R.string.get_register_code_tip);
                return;
            }
            if (TextUtils.isEmpty(activateState) || ("1".equals(activateState) && TextUtils.isEmpty(activateCode))){
                ToastUtils.showShort(R.string.get_activate_code_tip);
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
                id = data.getId();
                binding.tvRegisterCode.setText(registrationCode);
                binding.tvRegisterCode.setTextColor(getResources().getColor(R.color.black));
            }
            return;
        }
        ToastUtils.showShort(classBrandInfoRsp.getMsg());
    }

    @Override
    public void findRegistrationCodeFail(String msg) {
        Log.e(TAG, "findRegistrationCodeFail: " + msg);
        ToastUtils.showShort(msg);
    }

    @Override
    public void updateRegistrationCodeSuccess(BaseRsp baseRsp) {
        Log.e(TAG, "updateRegistrationCodeSuccess: " + baseRsp.toString());
        if (baseRsp.getCode() == 200) {
            //绑定成功，进入选择
            Intent intent = new Intent(this, ConfirmLoginActivity.class);
            intent.putExtra("brandStatus", brandStatus);
            intent.putExtra("code", code);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void updateRegistrationCodeFail(String msg) {
        Log.e(TAG, "updateRegistrationCodeFail: " + msg);
    }

    @Override
    public void findActivationCodeSuccess(ActivateRsp activateRsp) {
        Log.e(TAG, "findActivationCodeSuccess: "+activateRsp.toString() );
        if (activateRsp.getCode() == 200) {
            final ActivateRsp.DataBean data = activateRsp.getData();
            if (data == null){
                return;
            }
            activateState = data.getActivateState();
            activateCode = data.getActivateCode();
            //激活状态（1：已启用，2：禁用）
            if ("2".equals(activateState)){
                binding.tvActiveCode.setText(R.string.face_recongize_disable_tip);
                return;
            }
            binding.tvActiveCode.setText(activateCode);
            binding.tvActiveCode.setTextColor(getResources().getColor(R.color.black));
            return;
        }
        activateState = "2";
        ToastUtils.showShort(activateRsp.getMsg());
    }

    @Override
    public void findActivationCodeFail(String msg) {
        Log.e(TAG, "findActivationCodeFail: "+msg );
    }
}