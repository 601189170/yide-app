package com.yyide.chatim.presenter;


import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.DeviceUpdataView;
import com.yyide.chatim.view.getstuasView;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class DeviceUpdataPresenter extends BasePresenter<DeviceUpdataView> {

    public DeviceUpdataPresenter(DeviceUpdataView view) {
        attachView(view);
    }

//    public void getMyData(String machineCode,String deviceDirection,int officeId) {
//        mvpView.showLoading();
//        addSubscription(dingApiStores.setpm(machineCode,deviceDirection,officeId), new ApiCallback<DeviceUpdateRsp>() {
//            @Override
//            public void onSuccess(DeviceUpdateRsp model) {
//                mvpView.getData(model);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mvpView.getDataFail(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                mvpView.hideLoading();
//            }
//        });
//    }


}
