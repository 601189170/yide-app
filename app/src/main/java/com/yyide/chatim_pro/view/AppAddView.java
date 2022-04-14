package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AppAddRsp;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AppAddView extends BaseView {
    void getAppAppListSuccess(AppAddRsp model);

    void getAppAppListFail(String msg);

    void addAppSuccess(ResultBean model);

    void addAppFail(String msg);
}
