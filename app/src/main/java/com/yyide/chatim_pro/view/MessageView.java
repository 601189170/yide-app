package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AppAddRsp;
import com.yyide.chatim_pro.model.MessageNumberRsp;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.model.TodoRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface MessageView extends BaseView {

    void messageNumberSuccess(TodoRsp model);

    void messageNumberFail(String msg);
}
