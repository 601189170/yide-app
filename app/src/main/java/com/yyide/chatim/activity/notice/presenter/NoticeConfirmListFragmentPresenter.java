package com.yyide.chatim.activity.notice.presenter;

import com.yyide.chatim.activity.notice.view.NoticeConfirmListFragmentView;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ConfirmDetailRsp;
import com.yyide.chatim.net.ApiCallback;

public class NoticeConfirmListFragmentPresenter extends BasePresenter<NoticeConfirmListFragmentView> {
    public NoticeConfirmListFragmentPresenter(NoticeConfirmListFragmentView view) {
        attachView(view);
    }

    public void getConfirmDetails(int confirmType,long signId,int current,int size){
        mvpView.showLoading();
        addSubscription(dingApiStores.getConfirmDetails(confirmType,signId,current,size),new ApiCallback<ConfirmDetailRsp>(){

            @Override
            public void onSuccess(ConfirmDetailRsp model) {
                mvpView.noticeConfirmList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.noticeConfirmListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
