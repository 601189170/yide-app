package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.NoticeBlankReleaseBean;
import com.yyide.chatim_pro.model.NoticeReleaseTemplateBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.NoticeTemplateGeneralView;

import okhttp3.RequestBody;

public class NoticeTemplateGeneralPresenter extends BasePresenter<NoticeTemplateGeneralView> {
    public NoticeTemplateGeneralPresenter(NoticeTemplateGeneralView view) {
        attachView(view);
    }

    public void templateDetail(long id) {
        mvpView.showLoading();
        addSubscription(dingApiStores.templateDetail(id), new ApiCallback<NoticeReleaseTemplateBean>() {
            @Override
            public void onSuccess(NoticeReleaseTemplateBean model) {
                mvpView.getTemplateDetail(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getTemplateDetailFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void releaseNotice(NoticeBlankReleaseBean params) {
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(params));
        mvpView.showLoading();
        addSubscription(dingApiStores.releaseNotice(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.pushTemplateSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getTemplateDetailFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
