package com.yyide.chatim.activity.notice.presenter;

import android.util.Log;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeDetailView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.NoticeDetailRsp;
import com.yyide.chatim.net.ApiCallback;

public class NoticeDetailPresenter extends BasePresenter<NoticeDetailView> {
    public NoticeDetailPresenter(NoticeDetailView view) {
        attachView(view);
    }

    public void noticeDetail(long id, long signId, int type) {
        Log.e("NoticeDetailPresenter", "noticeDetail: " + id + ",type=" + type);
        mvpView.showLoading();
        if (type == 1) {
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
        } else if (type == 4) {
            addSubscription(dingApiStores.getMyNoticeBySignId(signId), new ApiCallback<NoticeDetailRsp>() {
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
        } else {
            addSubscription(dingApiStores.getMyReleaseNotice(id), new ApiCallback<NoticeDetailRsp>() {
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

    public void updateMyNoticeDetails(long id) {
        mvpView.showLoading();
        addSubscription(dingApiStores.updateMyNoticeDetails(id), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.updateMyNotice(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.updateFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
