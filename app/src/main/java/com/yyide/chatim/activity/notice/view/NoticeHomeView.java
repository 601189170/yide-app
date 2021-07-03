package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;

public interface NoticeHomeView extends BaseView {
    void noticeHome(NoticeMyReleaseDetailBean homeNoticeRsp);
    void noticeHomeFail(String msg);
}
