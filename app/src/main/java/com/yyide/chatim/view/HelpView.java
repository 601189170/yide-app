package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.model.HelpItemRep;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface HelpView extends BaseView {
    void getHelpListSuccess(HelpItemRep model);
    void getHelpListFail(String msg);
}
