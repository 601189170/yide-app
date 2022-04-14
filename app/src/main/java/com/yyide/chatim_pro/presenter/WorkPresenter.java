package com.yyide.chatim_pro.presenter;

import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.SelectSchByTeaidRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.WorkView;

public class WorkPresenter extends BasePresenter<WorkView> {
    public WorkPresenter(WorkView view) {
        attachView(view);
    }

    public void getWorkInfo(String id) {
//        mvpView.showLoading();
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
