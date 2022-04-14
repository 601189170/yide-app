package com.yyide.chatim_pro.activity.notice.presenter;

import com.yyide.chatim_pro.activity.notice.view.PublishAnnouncementFragmentView;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.NoticeListRsp;
import com.yyide.chatim_pro.net.ApiCallback;

public class PublishAnnouncementFragmentPresenter extends BasePresenter<PublishAnnouncementFragmentView> {
    public PublishAnnouncementFragmentPresenter(PublishAnnouncementFragmentView view) {
        attachView(view);
    }

    public void noticeList(int type, int page, int size) {
       // mvpView.showLoading();
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
                //mvpView.hideLoading();
            }
        });
    }

    /**
     * 删除我发布的公告
     * @param id
     */
    public void delAnnouncement(long id){
        mvpView.showLoading();
        addSubscription(dingApiStores.delAnnouncement(id), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.deleteAnnouncement(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.deleteFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
