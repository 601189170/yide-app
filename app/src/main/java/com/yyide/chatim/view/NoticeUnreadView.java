package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeUnreadPeopleBean;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeUnreadView extends BaseView {
    void pushNotice(ResultBean model);
    void getPushNoticeFail(String msg);
}
