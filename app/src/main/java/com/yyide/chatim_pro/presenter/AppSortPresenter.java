package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.AppSortParamsBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.AppSortView;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class AppSortPresenter extends BasePresenter<AppSortView> {
    public AppSortPresenter(AppSortView view) {
        attachView(view);
    }

    public void appSort(List<AppSortParamsBean> mapParams) {
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(mapParams));
        mvpView.showLoading();
        addSubscription(dingApiStores.appSort(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.sendAppSortSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.sendAppSortFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
