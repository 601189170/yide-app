package com.yyide.chatim_pro.activity.scancode;

import android.os.Bundle;
import android.view.View;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.base.BaseActivity;
import com.yyide.chatim_pro.base.BaseMvpActivity;
import com.yyide.chatim_pro.databinding.ActivityBindingEquipmentBinding;
import com.yyide.chatim_pro.databinding.ActivityConfirmSuccessBinding;
import com.yyide.chatim_pro.presenter.scan.ConfirmLoginPresenter;
import com.yyide.chatim_pro.view.scan.ConfirmLoginView;
/**
 * 扫码登录
 * @author liu tao
 * @date 2021年8月5日18:07:05
 */
public class ConfirmSuccessActivity extends BaseActivity {
    private static final String TAG = ConfirmSuccessActivity.class.getSimpleName();
    private ActivityConfirmSuccessBinding binding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_confirm_success;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.top.title.setText(R.string.app_scan_code);
        binding.top.backLayout.setVisibility(View.GONE);
        binding.btnEnter.setOnClickListener(v -> finish());
    }
}