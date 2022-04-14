package com.yyide.chatim_pro.view.leave;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.LeaveApprovalBean;
import com.yyide.chatim_pro.model.LeavePhraseRsp;

public interface StaffAskLeaveView extends BaseView {
    void approver(LeaveApprovalBean approverRsp);

    void approverFail(String msg);

    void addLeave(BaseRsp baseRsp);

    void addLeaveFail(String msg);

    void leavePhrase(LeavePhraseRsp leavePhraseRsp);

    void leavePhraseFail(String msg);

    void getClass(LeaveApprovalBean leaveApprovalBean);
}
