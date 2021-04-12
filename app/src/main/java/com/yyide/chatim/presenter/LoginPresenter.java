package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.VideoEntity;
import com.yyide.chatim.model.mobileRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.LoginView;
import com.yyide.chatim.view.MainView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public LoginPresenter(LoginView view) {
        attachView(view);
    }

    //账号密码登入
    public void Login(String a, String b) {
        mvpView.showLoading();
        addSubscription(dingApiStores.login(a, b), new ApiCallback<LoginRsp>() {
            @Override
            public void onSuccess(LoginRsp model) {
                mvpView.getData(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    //验证码登入
    public void loginmobile(String a, String b) {
        mvpView.showLoading();
        addSubscription(dingApiStores.loginmobile(a, b), new ApiCallback<mobileRsp>() {
            @Override
            public void onSuccess(mobileRsp model) {
                mvpView.loginmobileData(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getmobileFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    //获取验证码
    public void getcode(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        mvpView.showLoading();
        addSubscription(dingApiStores.getCode(body), new ApiCallback<SmsVerificationRsp>() {
            @Override
            public void onSuccess(SmsVerificationRsp model) {
                mvpView.getcode(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getcodeFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
