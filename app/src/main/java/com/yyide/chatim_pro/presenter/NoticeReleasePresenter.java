package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.NoticeBlankReleaseBean;
import com.yyide.chatim_pro.model.NoticeUnreadPeopleBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.NoticeBlankReleaseView;
import com.yyide.chatim_pro.view.NoticeUnreadPeopleView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class NoticeReleasePresenter extends BasePresenter<NoticeBlankReleaseView> {
    public NoticeReleasePresenter(NoticeBlankReleaseView view) {
        attachView(view);
    }

    public void releaseNotice(NoticeBlankReleaseBean params) {
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(params));
        mvpView.showLoading();
        addSubscription(dingApiStores.releaseNotice(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.getBlankReleaseSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getBlankReleaseFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
