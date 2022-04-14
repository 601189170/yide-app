package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.HelpItemRep;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface HelpIntroductionView extends BaseView {
    void getHelpListSuccess(HelpItemRep model);
    void getHelpListFail(String msg);
}
