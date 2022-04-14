package com.yyide.chatim_pro.presenter;

import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.MyAppListRsp;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.AppManagerView;

import okhttp3.RequestBody;

public class AppMannagerPresenter extends BasePresenter<AppManagerView> {
    public AppMannagerPresenter(AppManagerView view) {
        attachView(view);
    }

    public void getMyAppList() {
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
        addSubscription(dingApiStores.getMyApp(body), new ApiCallback<MyAppListRsp>() {
            @Override
            public void onSuccess(MyAppListRsp model) {
                if (model.isSuccess()) {
                    mvpView.getMyAppListSuccess(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getMyAppFail(msg);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    public void deleteApp(long id) {
        mvpView.showLoading();
        addSubscription(dingApiStores.deleteApp(id), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.deleteSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.deleteFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
