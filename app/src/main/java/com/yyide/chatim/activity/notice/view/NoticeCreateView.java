package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AddUserAnnouncementResponse;

public interface NoticeCreateView extends BaseView {

    void addUserAnnouncement(AddUserAnnouncementResponse addUserAnnouncementResponse);
    void addUserAnnouncementFail(String msg);
}
