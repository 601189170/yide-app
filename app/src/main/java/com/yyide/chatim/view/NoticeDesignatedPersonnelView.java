package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticePersonnelBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeDesignatedPersonnelView extends BaseView {
    void getDesignatedPersonnelList(NoticePersonnelBean model);

    void getDesignatedPersonnelFail(String msg);
}
