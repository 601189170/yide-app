package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.UserSigRsp;

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

}
