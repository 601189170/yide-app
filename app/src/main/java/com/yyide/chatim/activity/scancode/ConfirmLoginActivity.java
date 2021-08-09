package com.yyide.chatim.activity.scancode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.BookSearchActivity;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.ActivityBindingEquipmentBinding;
import com.yyide.chatim.databinding.ActivityConfirmLoginBinding;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.BrandSearchRsp;
import com.yyide.chatim.presenter.scan.BindingEquipmentPresenter;
import com.yyide.chatim.presenter.scan.ConfirmLoginPresenter;
import com.yyide.chatim.view.scan.BindingEquipmentView;
import com.yyide.chatim.view.scan.ConfirmLoginView;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫码登录
 *
 * @author liu tao
 * @date 2021年8月5日18:07:05
 */
public class ConfirmLoginActivity extends BaseMvpActivity<ConfirmLoginPresenter> implements ConfirmLoginView {
    private static final String TAG = ConfirmLoginActivity.class.getSimpleName();
    private ActivityConfirmLoginBinding binding;
    private String code;
    private String brandStatus;
    private BaseQuickAdapter<BrandSearchRsp.DataBean, BaseViewHolder> adapter;
    private List<BrandSearchRsp.DataBean> dataBeanList = new ArrayList<>();
    private String loginName;

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
        final Intent intent = getIntent();
        brandStatus = intent.getStringExtra("brandStatus");
        code = intent.getStringExtra("code");
        binding.top.backLayout.setOnClickListener(v -> finish());
        binding.btnLogin.setOnClickListener(v -> login());
        binding.etRegisterCode.setOnEditorActionListener((v, actionId, event) -> {
            Log.e(TAG, "onEditorAction: " + actionId);
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) binding.etRegisterCode.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(ConfirmLoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String keyWord = binding.etRegisterCode.getText().toString();
                if (TextUtils.isEmpty(keyWord)) {
                    ToastUtils.showShort(getString(R.string.search_class_brand_name));
                    return true;
                }
                mvpPresenter.getClassBrand(brandStatus, keyWord);
                return true;
            }
            return false;
        });

        binding.rvClassBrand.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<BrandSearchRsp.DataBean, BaseViewHolder>(R.layout.item_brand_info) {

            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, BrandSearchRsp.DataBean dataBean) {
                baseViewHolder.setText(R.id.tv_brand_name, dataBean.getSignName())
                        .setText(R.id.tv_login_status, "0".equals(dataBean.getStatus()) ? "已登录" : "未登录");
                baseViewHolder.setTextColor(R.id.tv_login_status, "0".equals(dataBean.getStatus()) ?
                        getResources().getColor(R.color.blue11) : getResources().getColor(R.color.text_909399));
                baseViewHolder.itemView.setSelected(dataBean.isChecked());
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.e(TAG, "onItemClick: "+position );
            final BrandSearchRsp.DataBean dataBean = dataBeanList.get(position);
            loginName = dataBean.getLoginName();
            for (BrandSearchRsp.DataBean bean : dataBeanList) {
                bean.setChecked(false);
            }
            dataBean.setChecked(true);
            adapter.notifyDataSetChanged();
        });
        binding.rvClassBrand.setAdapter(adapter);
        mvpPresenter.getClassBrand(brandStatus, "");
    }

    private void login() {
        if (TextUtils.isEmpty(loginName)){
            ToastUtils.showShort("请选择登录的班牌");
            return;
        }
        mvpPresenter.qrcodeLoginVerify(code,loginName);
    }

    @Override
    protected ConfirmLoginPresenter createPresenter() {
        return new ConfirmLoginPresenter(this);
    }

    @Override
    public void showError() {

    }

    @Override
    public void getClassBrandSuccess(BrandSearchRsp brandSearchRsp) {
        Log.e(TAG, "getClassBrandSuccess: " + brandSearchRsp.toString());
        if (brandSearchRsp.getCode() == 200) {
            final List<BrandSearchRsp.DataBean> data = brandSearchRsp.getData();
            if (data != null){
                dataBeanList.clear();
                dataBeanList.addAll(data);
                adapter.setList(dataBeanList);
            }
        }
    }

    @Override
    public void getClassBrandFail(String msg) {

    }

    @Override
    public void loginSuccess(BaseRsp baseRsp) {
        Log.e(TAG, "loginSuccess: "+baseRsp.toString() );
        ToastUtils.showShort(baseRsp.getMsg());
        if (baseRsp.getCode() == 200){
            jupm(this, ConfirmSuccessActivity.class);
            finish();
        }
    }

    @Override
    public void loginFail(String msg) {
        Log.e(TAG, "loginFail: "+msg );
        ToastUtils.showShort(msg);
        finish();
    }
}