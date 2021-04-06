package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.HomeNoticeRsp;

public interface NoticeHomeView extends BaseView {
    void noticeHome(HomeNoticeRsp homeNoticeRsp);
    void noticeHomeFail(String msg);
}
