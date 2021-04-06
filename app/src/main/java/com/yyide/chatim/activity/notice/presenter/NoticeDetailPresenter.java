package com.yyide.chatim.activity.notice.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeDetailView;
import com.yyide.chatim.model.NoticeDetailRsp;
import com.yyide.chatim.net.ApiCallback;

public class NoticeDetailPresenter extends BasePresenter<NoticeDetailView> {
    public NoticeDetailPresenter(NoticeDetailView view) {
        attachView(view);
    }

    public void noticeDetail(int id){
        mvpView.showLoading();
        addSubscription(dingApiStores.getMyNoticeDetails(id), new ApiCallback<NoticeDetailRsp>() {
            @Override
            public void onSuccess(NoticeDetailRsp model) {
                mvpView.noticeDetail(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.noticeDetailFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
