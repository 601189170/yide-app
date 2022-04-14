package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeItemBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeMyReleasedView extends BaseView {
    void getMyReceivedList(NoticeItemBean model);

    void getMyReceivedFail(String msg);
}
