package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.AppAddRsp;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.AppAddView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class AppAddPresenter extends BasePresenter<AppAddView> {
    public AppAddPresenter(AppAddView view) {
        attachView(view);
    }

    public void getAppAddList(int size, int pageNum) {
//        mvpView.showLoading();
        Map<String, Integer> map = new HashMap<>();
        map.put("size", size);
        map.put("current", pageNum);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getAppAddList(body), new ApiCallback<AppAddRsp>() {
            @Override
            public void onSuccess(AppAddRsp model) {
                mvpView.getAppAppListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getAppAppListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void addApp(long id) {
//        mvpView.showLoading();
        addSubscription(dingApiStores.addApp(id), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.addAppSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addAppFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
