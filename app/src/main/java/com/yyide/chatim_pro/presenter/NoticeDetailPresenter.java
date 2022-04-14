package com.yyide.chatim_pro.presenter;

import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.NoticeDetailView;

public class NoticeDetailPresenter extends BasePresenter<NoticeDetailView> {
    public NoticeDetailPresenter(NoticeDetailView view) {
        attachView(view);
    }

    public void getPublishDetail(long id) {
        mvpView.showLoading();
        addSubscription(dingApiStores.publishDetail(id), new ApiCallback<NoticeMyReleaseDetailBean>() {
            @Override
            public void onSuccess(NoticeMyReleaseDetailBean model) {
                mvpView.getMyReceivedList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getMyReceivedFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * 撤回
     * @param id
     */
    public void retract(long id) {
        mvpView.showLoading();
        addSubscription(dingApiStores.retract(id), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.retractSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getMyReceivedFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
