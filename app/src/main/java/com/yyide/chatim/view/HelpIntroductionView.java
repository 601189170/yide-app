package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.HelpItemRep;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface HelpIntroductionView extends BaseView {
    void getHelpListSuccess(HelpItemRep model);
    void getHelpListFail(String msg);

    void getHelpAdvancedSuccess(HelpItemRep model);
    void getHelpAdvancedFails(String msg);
}
