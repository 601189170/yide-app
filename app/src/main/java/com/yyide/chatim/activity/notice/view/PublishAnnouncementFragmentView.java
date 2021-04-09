package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.NoticeListRsp;

public interface PublishAnnouncementFragmentView extends BaseView {
    void noticeList(NoticeListRsp noticeListRsp);
    void noticeListFail(String msg);

    void deleteAnnouncement(BaseRsp baseRsp);
    void deleteFail(String msg);
}
