package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeItemBean;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeDetailView extends BaseView {

    void retractSuccess(ResultBean model);
    void getMyReceivedList(NoticeMyReleaseDetailBean model);

    void getMyReceivedFail(String msg);
}
