package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：Lrz on 2021年4月5日
 */
public interface RegisterView extends BaseView {
    void registerSuccess(ResultBean msg);
    void getSmsSuccess(ResultBean msg);
    void fail(String msg);
}
