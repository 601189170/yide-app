package com.yyide.chatim.activity.notice.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeCreateView;
import com.yyide.chatim.model.AddUserAnnouncementBody;
import com.yyide.chatim.model.AddUserAnnouncementResponse;
import com.yyide.chatim.net.ApiCallback;

import okhttp3.RequestBody;

public class NoticeCreatePresenter extends BasePresenter<NoticeCreateView> {
    public NoticeCreatePresenter(NoticeCreateView view) {
        attachView(view);
    }

    public void addUserAnnouncement(String json) {
        mvpView.showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        addSubscription(dingApiStores.addUserAnnouncement(body), new ApiCallback<AddUserAnnouncementResponse>() {
            @Override
            public void onSuccess(AddUserAnnouncementResponse model) {
                mvpView.addUserAnnouncement(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addUserAnnouncementFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
