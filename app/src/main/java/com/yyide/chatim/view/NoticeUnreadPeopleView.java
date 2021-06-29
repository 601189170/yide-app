package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticeUnreadPeopleBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeUnreadPeopleView extends BaseView {
    void getUnreadPeopleList(NoticeUnreadPeopleBean model);

    void getUnreadPeopleFail(String msg);
}
