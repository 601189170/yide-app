package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.mobileRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface LoginView extends BaseView {

    void getData(LoginRsp rsp);

    void getDataFail(String msg);

    void loginmobileData(mobileRsp rsp);

    void getmobileFail(String msg);

    void getcode(SmsVerificationRsp rsp);

    void getcodeFail(String msg);

}
