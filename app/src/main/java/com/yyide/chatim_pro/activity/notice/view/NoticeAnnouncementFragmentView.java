package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeListRsp;

public interface NoticeAnnouncementFragmentView extends BaseView {
    void noticeList(NoticeListRsp noticeListRsp);
    void noticeListFail(String msg);
}
