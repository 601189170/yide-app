package com.yyide.chatim_pro.view.leave;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.ApproverRsp;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.LeaveDeptRsp;
import com.yyide.chatim_pro.model.LeavePhraseRsp;

public interface StudentAskLeaveView extends BaseView {
    void approver(ApproverRsp approverRsp);
    void approverFail(String msg);

    void addStudentLeave(BaseRsp baseRsp);
    void addStudentLeaveFail(String msg);

    void leavePhrase(LeavePhraseRsp leavePhraseRsp);
    void leavePhraseFail(String msg);
}
