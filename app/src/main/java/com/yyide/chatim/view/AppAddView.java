package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AppAddView extends BaseView {
    void getAppAppListSuccess(AppAddRsp model);

    void getAppAppListFail(String msg);

    void addAppSuccess(ResultBean model);

    void addAppFail(String msg);
}
