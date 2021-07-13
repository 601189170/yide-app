package com.yyide.chatim.view.leave;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.model.NoticeListRsp;

public interface StaffAskLeaveView extends BaseView {
    void leaveDept(LeaveDeptRsp leaveDeptRsp);
    void leaveDeptFail(String msg);

    void approver(ApproverRsp approverRsp);
    void approverFail(String msg);

    void addTeacherLeave(BaseRsp baseRsp);
    void addTeacherLeaveFail(String msg);

    void leavePhrase(LeavePhraseRsp leavePhraseRsp);
    void leavePhraseFail(String msg);
}
