package com.yyide.chatim_pro.view.leave;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.LeaveDetailRsp;
import com.yyide.chatim_pro.model.LeaveListRsp;

public interface LeaveDetailView extends BaseView {
    void leaveDetail(LeaveDetailRsp leaveDetailRsp);

    void leaveDetailFail(String msg);

    void repealResult(BaseRsp baseRsp);

    void repealFail(String msg);

    void processApproval(BaseRsp baseRsp);

    void processApprovalFail(String msg);

    void leaveBack(BaseRsp baseRsp);
}
