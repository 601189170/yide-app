package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeListRsp;

public interface NoticeAnnouncementFragmentView extends BaseView {
    void noticeList(NoticeListRsp noticeListRsp);
    void noticeListFail(String msg);
}
