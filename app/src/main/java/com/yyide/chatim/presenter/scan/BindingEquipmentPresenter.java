package com.yyide.chatim.presenter.scan;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.ClassBrandInfoRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.LeaveDetailView;
import com.yyide.chatim.view.scan.BindingEquipmentView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class BindingEquipmentPresenter extends BasePresenter<BindingEquipmentView> {
    public BindingEquipmentPresenter(BindingEquipmentView view) {
        attachView(view);
    }

    public void getRegistrationCodeByOffice() {
        mvpView.showLoading();
        addSubscription(dingApiStores.getRegistrationCodeByOffice(), new ApiCallback<ClassBrandInfoRsp>() {
            @Override
            public void onSuccess(ClassBrandInfoRsp model) {
                mvpView.findRegistrationCodeSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.findRegistrationCodeFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * app注册码绑定
     *
     * @param id
     * @param status
     * @param equipmentSerialNumber
     * @param registrationCode
     */
    public void updateRegistrationCodeByCode(String id, String status, String equipmentSerialNumber, String registrationCode) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("id", id);
        map.put("status", status);
        map.put("equipmentSerialNumber", equipmentSerialNumber);
        map.put("registrationCode", registrationCode);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.updateRegistrationCodeByCode(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.updateRegistrationCodeSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.updateRegistrationCodeFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
