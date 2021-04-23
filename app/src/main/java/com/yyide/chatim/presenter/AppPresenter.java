package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AppView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class AppPresenter extends BasePresenter<AppView> {
    public AppPresenter(AppView view) {
        attachView(view);
    }

    public void getMyAppList() {
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
//        mvpView.showLoading();
        addSubscription(dingApiStores.getMyApp(body), new ApiCallback<AppListRsp>() {
            @Override
            public void onSuccess(AppListRsp model) {
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
                mvpView.hideLoading();
            }
        });
    }

    public void getAppList() {
//        {"size": 10,"current": 1}
        Map<String, Object> map = new HashMap<>();
        map.put("size", 100);
        map.put("current", 1);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
//        mvpView.showLoading();
        addSubscription(dingApiStores.getAppList(body), new ApiCallback<AppItemBean>() {
            @Override
            public void onSuccess(AppItemBean model) {
                mvpView.getAppListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getAppListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
