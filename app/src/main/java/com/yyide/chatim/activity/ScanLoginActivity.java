package com.yyide.chatim.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.presenter.ScanLoginPresenter;
import com.yyide.chatim.view.ScanLoginView;

import java.io.IOException;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScanLoginActivity extends BaseMvpActivity<ScanLoginPresenter> implements ScanLoginView {

    private String result;

    @Override
    public int getContentViewID() {
        return R.layout.activity_scan_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        result = getIntent().getStringExtra("result");
    }

    @OnClick({R.id.login, R.id.cancel})
    void click(View view) {
        switch (view.getId()) {
            case R.id.login:
                //mvpPresenter.scanLogin();
                loginScan(result);
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    OkHttpClient mOkHttpClient = new OkHttpClient();

    //获取学校信息
    void loginScan(String url) {

        //请求组合创建
        if (SpData.getLogin() == null) return;
        Request request = new Request.Builder()
                .url(BaseConstant.API_SERVER_URL + url)
                .addHeader("Authorization", SpData.getLogin() != null ? SpData.getLogin().accessToken : "")
                .build();
        showLoading();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideLoading();
                Log.e("scan", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                hideLoading();
                String data = response.body().string();
                Log.e("scan", "onResponse==>: " + data);
                finish();
            }
        });
    }

    @Override
    protected ScanLoginPresenter createPresenter() {
        return new ScanLoginPresenter(this);
    }

    @Override
    public void scanLoginSuccess(String msg) {

    }

    @Override
    public void scanLoginFail(String msg) {

    }

    @Override
    public void showError() {

    }
}