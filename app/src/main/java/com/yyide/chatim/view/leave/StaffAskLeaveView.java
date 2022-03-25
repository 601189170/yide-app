package com.yyide.chatim.view.leave;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveApprovalBean;
import com.yyide.chatim.model.LeavePhraseRsp;

public interface StaffAskLeaveView extends BaseView {
    void approver(LeaveApprovalBean approverRsp);

    void approverFail(String msg);

    void addLeave(BaseRsp baseRsp);

    void addLeaveFail(String msg);

    void leavePhrase(LeavePhraseRsp leavePhraseRsp);

    void leavePhraseFail(String msg);

    void getClass(LeaveApprovalBean leaveApprovalBean);
}
