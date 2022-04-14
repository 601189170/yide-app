package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.LoginAccountBean;
import com.yyide.chatim_pro.model.SmsVerificationRsp;
import com.yyide.chatim_pro.model.UserSigRsp;
import com.yyide.chatim_pro.model.LoginRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface LoginView extends BaseView {

    void getLoginSuccess(LoginRsp rsp);

    void loginMobileData(LoginRsp rsp);

    void getCode(SmsVerificationRsp rsp);

    void getUserSign(UserSigRsp model);

    void getFail(String msg);

    void getUserSchool(GetUserSchoolRsp rsp);

    void getAccountSwitch(LoginAccountBean model);

}
