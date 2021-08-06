package com.yyide.chatim.view.scan;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.ClassBrandInfoRsp;
import com.yyide.chatim.model.LeaveDetailRsp;

public interface BindingEquipmentView extends BaseView {
    void findRegistrationCodeSuccess(ClassBrandInfoRsp classBrandInfoRsp);
    void findRegistrationCodeFail(String msg);

    void updateRegistrationCodeSuccess(BaseRsp baseRsp);
    void updateRegistrationCodeFail(String msg);
}
