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
        binding.btnEnter.setOnClickListener(v -> {
            if ("0".equals(bindStatus)) {
                jupm(this, ConfirmLoginActivity.class, "brandStatus", brandStatus);
                finish();
                return;
            }
            //去绑定设备
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(registrationCode)) {
                ToastUtils.showShort("您还没有获取注册码！");
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
}