package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeItemBean;
import com.yyide.chatim_pro.model.NoticePersonnelBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeDesignatedPersonnelView extends BaseView {
    void getDesignatedPersonnelList(NoticePersonnelBean model);

    void getDesignatedPersonnelFail(String msg);
}
