package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticeReleaseTemplateBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeReleasedTemplateView extends BaseView {
    void getNoticeReleasedList(NoticeReleaseTemplateBean model);

    void getNoticeReleasedFail(String msg);
}
