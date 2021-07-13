package com.yyide.chatim.view.leave;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeaveListRsp;

public interface LeaveDetailView extends BaseView {
    void leaveDetail(LeaveDetailRsp leaveDetailRsp);
    void leaveDetailFail(String msg);
    void repealResult(BaseRsp baseRsp);
    void repealFail(String msg);
    void processApproval(BaseRsp baseRsp);
    void processApprovalFail(String msg);
}
