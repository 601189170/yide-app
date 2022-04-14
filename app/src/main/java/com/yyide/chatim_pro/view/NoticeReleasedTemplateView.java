package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeItemBean;
import com.yyide.chatim_pro.model.NoticeReleaseTemplateBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeReleasedTemplateView extends BaseView {
    void getNoticeReleasedList(NoticeReleaseTemplateBean model);
    void getNoticeReleasedClassifyList(NoticeReleaseTemplateBean model);
    void getNoticeReleasedFail(String msg);
}
