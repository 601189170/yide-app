package com.yyide.chatim.view.leave;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeListRsp;

public interface AskForLeaveListView extends BaseView {
    void leaveList(NoticeListRsp noticeListRsp);
    void leaveListFail(String msg);
}
