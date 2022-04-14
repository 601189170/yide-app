package com.yyide.chatim_pro.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.LoginAccountBean;
import com.yyide.chatim_pro.model.LoginRsp;
import com.yyide.chatim_pro.model.SmsVerificationRsp;
import com.yyide.chatim_pro.model.UserSigRsp;
import com.yyide.chatim_pro.model.mobileRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.LoginView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public LoginPresenter(LoginView view) {
        attachView(view);
    }

    //账号密码登入
    public void Login(@Body RequestBody info) {
        mvpView.showLoading();
        addSubscription(dingApiStores.login(info), new ApiCallback<LoginRsp>() {
            @Override
            public void onSuccess(LoginRsp model) {
                mvpView.getLoginSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    public void getUserSign() {
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
        addSubscription(dingApiStores.getUserSig(body), new ApiCallback<UserSigRsp>() {
            @Override
            public void onSuccess(UserSigRsp model) {
                mvpView.getUserSign(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    //验证码登入
    public void loginMobile(String mobile, String validateCode) {
        mvpView.showLoading();
        RequestBody body = new FormBody.Builder()
                .add("validateCode", validateCode)
                .add("mobile", mobile)
                .add("version", "2")
                .build();
        addSubscription(dingApiStores.loginmobile(body), new ApiCallback<LoginRsp>() {
            @Override
            public void onSuccess(LoginRsp model) {
                mvpView.loginMobileData(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    //获取验证码
    public void getCode(String phone) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getCode(body), new ApiCallback<SmsVerificationRsp>() {
            @Override
            public void onSuccess(SmsVerificationRsp model) {
                mvpView.getCode(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getUserSchool() {
        mvpView.showLoading();
        addSubscription(dingApiStores.getUserSchool(), new ApiCallback<GetUserSchoolRsp>() {
            @Override
            public void onSuccess(GetUserSchoolRsp model) {
                mvpView.getUserSchool(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    public void accountSwitch() {
        addSubscription(dingApiStores.accountSwitch("reg"), new ApiCallback<LoginAccountBean>() {
            @Override
            public void onSuccess(LoginAccountBean model) {
                mvpView.getAccountSwitch(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
            }
        });
    }

}
