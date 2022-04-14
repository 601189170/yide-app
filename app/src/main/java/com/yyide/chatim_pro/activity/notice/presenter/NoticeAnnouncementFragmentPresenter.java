package com.yyide.chatim_pro.activity.notice.presenter;

import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.activity.notice.view.NoticeAnnouncementFragmentView;
import com.yyide.chatim_pro.model.NoticeListRsp;
import com.yyide.chatim_pro.net.ApiCallback;

public class NoticeAnnouncementFragmentPresenter extends BasePresenter<NoticeAnnouncementFragmentView> {
    public NoticeAnnouncementFragmentPresenter(NoticeAnnouncementFragmentView view) {
        attachView(view);
    }

    public void noticeList(int type, int page, int size) {
        //mvpView.showLoading();
        addSubscription(dingApiStores.getMyNotice(type, page, size), new ApiCallback<NoticeListRsp>() {
            @Override
            public void onSuccess(NoticeListRsp model) {
                mvpView.noticeList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.noticeListFail(msg);
            }

            @Override
            public void onFinish() {
               // mvpView.hideLoading();
            }
        });
    }
}
