package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface ScanLoginView extends BaseView {
    void scanLoginSuccess(String msg);
    void scanLoginFail(String msg);
}
