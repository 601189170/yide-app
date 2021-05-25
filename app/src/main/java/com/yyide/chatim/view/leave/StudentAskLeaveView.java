package com.yyide.chatim.view.leave;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeavePhraseRsp;

public interface StudentAskLeaveView extends BaseView {
    void approver(ApproverRsp approverRsp);
    void approverFail(String msg);

    void addStudentLeave(BaseRsp baseRsp);
    void addStudentLeaveFail(String msg);

    void leavePhrase(LeavePhraseRsp leavePhraseRsp);
    void leavePhraseFail(String msg);
}
