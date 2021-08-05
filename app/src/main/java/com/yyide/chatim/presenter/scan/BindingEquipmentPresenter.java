package com.yyide.chatim.presenter.scan;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.LeaveDetailView;
import com.yyide.chatim.view.scan.BindingEquipmentView;

import java.util.HashMap;

public class BindingEquipmentPresenter extends BasePresenter<BindingEquipmentView> {
    public BindingEquipmentPresenter(BindingEquipmentView view) {
        attachView(view);
    }
}
