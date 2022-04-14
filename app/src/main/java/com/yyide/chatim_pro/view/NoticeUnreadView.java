package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeUnreadPeopleBean;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeUnreadView extends BaseView {
    void pushNotice(ResultBean model);
    void getPushNoticeFail(String msg);
}
