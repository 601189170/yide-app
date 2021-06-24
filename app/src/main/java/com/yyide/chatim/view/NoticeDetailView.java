package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeDetailView extends BaseView {

    void retractSuccess(ResultBean model);
    void getMyReceivedList(NoticeMyReleaseDetailBean model);

    void getMyReceivedFail(String msg);
}
