package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.ResetPasswordView;
import com.yyide.chatim.view.UserView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者：LRZ on 2021年4月5日
 */
public class ResetPasswordPresenter extends BasePresenter<ResetPasswordView> {

    public ResetPasswordPresenter(ResetPasswordView view) {
        attachView(view);
    }

    public void getSmsCode(String phone) {
        mvpView.showLoading();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobileNumber", phone);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(paramMap));
        addSubscription(dingApiStores.getForgotSmsCode(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                if (model.isSuccess()) {
                    mvpView.getSmsSuccess(model.getMsg());
                } else {
                    mvpView.getSmsFail(model.getMsg());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getSmsFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void updatePwd(String mobile, String code, String newPassword) {
        mvpView.showLoading();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobileNumber", mobile);
        paramMap.put("validateCode", code);
        paramMap.put("newPassword", newPassword);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(paramMap));
        addSubscription(dingApiStores.forgotPwd(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                if (model.isSuccess()) {
                    mvpView.updateSuccess(model.getMsg());
                } else {
                    mvpView.updateFail(model.getMsg());
                }

            }

            @Override
            public void onFailure(String msg) {
                mvpView.updateFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
