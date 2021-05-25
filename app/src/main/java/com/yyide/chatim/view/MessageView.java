package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.MessageNumberRsp;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface MessageView extends BaseView {

    void messageNumberSuccess(MessageNumberRsp model);

    void messageNumberFail(String msg);
}
