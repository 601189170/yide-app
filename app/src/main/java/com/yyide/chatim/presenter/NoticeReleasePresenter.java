package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NoticeBlankReleaseBean;
import com.yyide.chatim.model.NoticeUnreadPeopleBean;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoticeBlankReleaseView;
import com.yyide.chatim.view.NoticeUnreadPeopleView;

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
