package com.yyide.chatim.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.WorkView;

public class WorkPresenter extends BasePresenter<WorkView> {
    public WorkPresenter(WorkView view) {
        attachView(view);
    }

    public void getWorkInfo(String id) {
        mvpView.showLoading();
        addSubscription(dingApiStores.getClassesHomeworkInfo(id), new ApiCallback<SelectSchByTeaidRsp>() {
            @Override
            public void onSuccess(SelectSchByTeaidRsp model) {
                mvpView.getWorkSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getWorkFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
