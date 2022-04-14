package com.yyide.chatim_pro.presenter;


import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.DeviceUpdateRsp;
import com.yyide.chatim_pro.model.GetStuasRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.getstuasView;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class GetstuasPresenter extends BasePresenter<getstuasView> {


    public GetstuasPresenter(getstuasView view) {
        attachView(view);
    }

    public void getMyData() {

        mvpView.showLoading();
        addSubscription(dingApiStores.getData(), new ApiCallback<GetStuasRsp>() {
            @Override
            public void onSuccess(GetStuasRsp model) {
//                mvpView.hideLoading();
                mvpView.getData(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void getMyData2(String machineCode,String deviceDirection,int officeId) {
        mvpView.showLoading();
        addSubscription(dingApiStores.setpm(machineCode,officeId,deviceDirection), new ApiCallback<DeviceUpdateRsp>() {
            @Override
            public void onSuccess(DeviceUpdateRsp model) {
                mvpView.getData2(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDataFail2(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
