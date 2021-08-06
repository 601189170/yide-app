package com.yyide.chatim.presenter.scan;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.BrandSearchRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.scan.BindingEquipmentView;
import com.yyide.chatim.view.scan.ConfirmLoginView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class ConfirmLoginPresenter extends BasePresenter<ConfirmLoginView> {
    public ConfirmLoginPresenter(ConfirmLoginView view) {
        attachView(view);
    }

    /**
     * 请求班牌数据
     * @param status
     * @param signName
     */
    public void getClassBrand(String status,String signName){
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("signName", signName);
        map.put("status", status);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getClassBrand(body), new ApiCallback<BrandSearchRsp>() {
            @Override
            public void onSuccess(BrandSearchRsp model) {
                mvpView.getClassBrandSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getClassBrandFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * 扫码登录认证
     * @param code
     * @param userName
     */
    public void qrcodeLoginVerify(String code,String userName){
        mvpView.showLoading();
        addSubscription(dingApiStores.qrcodeLoginVerify(code, userName), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.loginSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.loginFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
