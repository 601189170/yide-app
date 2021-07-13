package com.yyide.chatim.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticePersonnelBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoticeDesignatedPersonnelView;
import com.yyide.chatim.view.NoticeReceivedView;

public class NoticeDesignatedPersonnelPresenter extends BasePresenter<NoticeDesignatedPersonnelView> {
    public NoticeDesignatedPersonnelPresenter(NoticeDesignatedPersonnelView view) {
        attachView(view);
    }

    public void specifieTypeList(String specifieType) {
        mvpView.showLoading();
        addSubscription(dingApiStores.specifieType(specifieType), new ApiCallback<NoticePersonnelBean>() {
            @Override
            public void onSuccess(NoticePersonnelBean model) {
                mvpView.getDesignatedPersonnelList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDesignatedPersonnelFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
