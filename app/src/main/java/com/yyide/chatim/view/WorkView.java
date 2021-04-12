package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.SelectSchByTeaidRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface WorkView extends BaseView {
    void getWorkSuccess(SelectSchByTeaidRsp model);

    void getWorkFail(String msg);

}
