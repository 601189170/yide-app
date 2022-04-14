package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.HomeNoticeRsp;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;

public interface NoticeHomeView extends BaseView {
    void noticeHome(NoticeMyReleaseDetailBean homeNoticeRsp);
    void noticeHomeFail(String msg);
}
