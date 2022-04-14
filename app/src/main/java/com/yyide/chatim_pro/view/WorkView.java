package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.SelectSchByTeaidRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface WorkView extends BaseView {
    void getWorkSuccess(SelectSchByTeaidRsp model);

    void getWorkFail(String msg);

}
