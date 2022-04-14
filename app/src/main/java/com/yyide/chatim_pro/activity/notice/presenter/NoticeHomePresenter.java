package com.yyide.chatim_pro.activity.notice.presenter;

import com.yyide.chatim_pro.activity.notice.view.NoticeHomeView;
import com.yyide.chatim_pro.activity.notice.view.NoticeScopeView;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.HomeNoticeRsp;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.net.ApiCallback;

public class NoticeHomePresenter extends BasePresenter<NoticeHomeView> {
    public NoticeHomePresenter(NoticeHomeView view) {
        attachView(view);
    }

    public void getHomeNotice(){
        addSubscription(dingApiStores.noticeShow(), new ApiCallback<NoticeMyReleaseDetailBean>() {
            @Override
            public void onSuccess(NoticeMyReleaseDetailBean model) {
                mvpView.noticeHome(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.noticeHomeFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
