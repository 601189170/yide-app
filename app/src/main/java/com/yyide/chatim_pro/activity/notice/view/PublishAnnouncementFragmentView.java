package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.NoticeListRsp;

public interface PublishAnnouncementFragmentView extends BaseView {
    void noticeList(NoticeListRsp noticeListRsp);
    void noticeListFail(String msg);

    void deleteAnnouncement(BaseRsp baseRsp);
    void deleteFail(String msg);
}
