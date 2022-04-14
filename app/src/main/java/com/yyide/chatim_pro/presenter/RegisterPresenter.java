package com.yyide.chatim_pro.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.RegisterView;
import com.yyide.chatim_pro.view.ResetPasswordView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者：LRZ on 2021年4月5日
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {

    public RegisterPresenter(RegisterView view) {
        attachView(view);
    }

    public void getSmsCode(String phone) {
        mvpView.showLoading();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobileNumber", phone);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(paramMap));
        addSubscription(dingApiStores.getRegisterSms(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.getSmsSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void register(String mobile, String code, String password) {
        mvpView.showLoading();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobileNumber", mobile);
        paramMap.put("validateCode", code);
        paramMap.put("password", password);
        //{"mobileNumber":"13267184935","validateCode":"255128","password":"a123456"}
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(paramMap));
        addSubscription(dingApiStores.register(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.registerSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
