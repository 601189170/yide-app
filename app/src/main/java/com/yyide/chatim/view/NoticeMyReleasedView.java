package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeItemBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeMyReleasedView extends BaseView {
    void getMyReceivedList(NoticeItemBean model);

    void getMyReceivedFail(String msg);
}
