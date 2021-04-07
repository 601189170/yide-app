package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AppView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class AppPresenter extends BasePresenter<AppView> {
    public AppPresenter(AppView view) {
        attachView(view);
    }

    public void getMyAppList(){
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
        mvpView.showLoading();
        addSubscription(dingApiStores.getMyApp(body), new ApiCallback() {
            @Override
            public void onSuccess(Object model) {
                mvpView.hideLoading();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void searchApp(){
//        {
//            "name": "str5",
//                "size": 10,
//                "current": 1
//        }
        Map<String, String> map = new HashMap<>();
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mvpView.showLoading();
        addSubscription(dingApiStores.searchApp(body), new ApiCallback() {
            @Override
            public void onSuccess(Object model) {
                mvpView.hideLoading();

            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
