package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AppItemBean;
import com.yyide.chatim_pro.model.MyAppListRsp;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AppSortView extends BaseView {
    void sendAppSortSuccess(ResultBean model);

    void sendAppSortFail(String msg);
}
