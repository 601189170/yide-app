package com.yyide.chatim.presenter;

import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoticeReceivedView;

import okhttp3.RequestBody;

public class NoticeReceivedPresenter extends BasePresenter<NoticeReceivedView> {
    public NoticeReceivedPresenter(NoticeReceivedView view) {
        attachView(view);
    }

    public void getMyAppList() {
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
//        mvpView.showLoading();
        addSubscription(dingApiStores.getMyApp(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
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

}
