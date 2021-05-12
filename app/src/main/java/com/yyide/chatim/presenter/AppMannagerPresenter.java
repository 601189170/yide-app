package com.yyide.chatim.presenter;

import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AppManagerView;

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

    public void deleteApp(int id) {
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
