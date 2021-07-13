package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;

/**
 * 作者：Lrz on 2021年4月5日
 */
public interface ResetPasswordView extends BaseView {
    void updateSuccess(String msg);
    void updateFail(String msg);
    void getSmsSuccess(String msg);
    void getSmsFail(String msg);

}
