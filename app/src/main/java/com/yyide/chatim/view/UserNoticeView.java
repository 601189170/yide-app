package com.yyide.chatim.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.NoticeDetailRsp;
import com.yyide.chatim.model.UserMsgNoticeRsp;
import com.yyide.chatim.model.UserNoticeRsp;

public interface UserNoticeView extends BaseView {

    void getUserNoticePageSuccess(UserMsgNoticeRsp userNoticeRsp);
    void getUserNoticePageFail(String msg);

}
