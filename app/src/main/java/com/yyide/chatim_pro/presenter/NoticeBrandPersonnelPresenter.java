package com.yyide.chatim_pro.presenter;

import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.NoticeBrandBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.NoticeBrandPersonnelView;

public class NoticeBrandPersonnelPresenter extends BasePresenter<NoticeBrandPersonnelView> {
    public NoticeBrandPersonnelPresenter(NoticeBrandPersonnelView view) {
        attachView(view);
    }

    public void notificationClasses() {
        mvpView.showLoading();
        addSubscription(dingApiStores.notificationClasses(), new ApiCallback<NoticeBrandBean>() {
            @Override
            public void onSuccess(NoticeBrandBean model) {
                mvpView.getBrandPersonnelList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getBrandPersonnelFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void notificationSite() {
        mvpView.showLoading();
        addSubscription(dingApiStores.notificationSite(), new ApiCallback<NoticeBrandBean>() {
            @Override
            public void onSuccess(NoticeBrandBean model) {
                mvpView.getSitePersonnelList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getBrandPersonnelFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
