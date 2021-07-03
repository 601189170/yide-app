package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NoticeUnreadPeopleBean;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoticeUnreadPeopleView;
import com.yyide.chatim.view.NoticeUnreadView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class NoticeUnreadPresenter extends BasePresenter<NoticeUnreadView> {
    public NoticeUnreadPresenter(NoticeUnreadView view) {
        attachView(view);
    }

    public void unNoticeNotify(long id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mvpView.showLoading();
        addSubscription(dingApiStores. unNoticeNotify(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.pushNotice(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getPushNoticeFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
