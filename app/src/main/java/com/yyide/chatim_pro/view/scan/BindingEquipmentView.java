package com.yyide.chatim_pro.view.scan;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.ActivateRsp;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.ClassBrandInfoRsp;
import com.yyide.chatim_pro.model.LeaveDetailRsp;

public interface BindingEquipmentView extends BaseView {
    void findRegistrationCodeSuccess(ClassBrandInfoRsp classBrandInfoRsp);
    void findRegistrationCodeFail(String msg);

    void updateRegistrationCodeSuccess(BaseRsp baseRsp);
    void updateRegistrationCodeFail(String msg);

    void findActivationCodeSuccess(ActivateRsp activateRsp);
    void findActivationCodeFail(String msg);
}
