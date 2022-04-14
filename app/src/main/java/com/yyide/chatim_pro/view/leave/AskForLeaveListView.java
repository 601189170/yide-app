package com.yyide.chatim_pro.view.leave;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.LeaveListRsp;
import com.yyide.chatim_pro.model.NoticeListRsp;

public interface AskForLeaveListView extends BaseView {
    void leaveList(LeaveListRsp leaveListRsp);
    void leaveListFail(String msg);
}
