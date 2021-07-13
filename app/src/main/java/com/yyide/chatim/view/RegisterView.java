package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：Lrz on 2021年4月5日
 */
public interface RegisterView extends BaseView {
    void registerSuccess(ResultBean msg);
    void getSmsSuccess(ResultBean msg);
    void fail(String msg);
}
