package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.mobileRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface UserView extends BaseView {
    void updateSuccess(String msg);
    void updateFail(String msg);
    void uploadFileSuccess(String imgUrl);
    void uploadFileFail(String msg);
}
