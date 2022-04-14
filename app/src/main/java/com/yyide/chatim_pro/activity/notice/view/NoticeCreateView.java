package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AddUserAnnouncementResponse;

public interface NoticeCreateView extends BaseView {

    void addUserAnnouncement(AddUserAnnouncementResponse addUserAnnouncementResponse);
    void addUserAnnouncementFail(String msg);
}
