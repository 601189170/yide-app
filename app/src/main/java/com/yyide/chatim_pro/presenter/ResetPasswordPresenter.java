package com.yyide.chatim_pro.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.ResetPasswordView;
import com.yyide.chatim_pro.view.UserView;

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
                    mvpView.getSmsSuccess(model.getMessage());
                } else {
                    mvpView.getSmsFail(model.getMessage());
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

    public void updatePwd(String oldPwd, String newPassword, String confirmPwd) {
        mvpView.showLoading();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("oldPwd", oldPwd);
        paramMap.put("newPwd", newPassword);
        paramMap.put("confirmPwd", confirmPwd);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(paramMap));
        addSubscription(dingApiStores.forgotPwd(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCESS2) {
                    mvpView.updateSuccess(model.getMessage());
                } else {
                    mvpView.updateFail(model.getMessage());
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
