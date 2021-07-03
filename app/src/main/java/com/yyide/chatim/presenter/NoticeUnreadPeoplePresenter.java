package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticeUnreadPeopleBean;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoticeReceivedView;
import com.yyide.chatim.view.NoticeUnreadPeopleView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class NoticeUnreadPeoplePresenter extends BasePresenter<NoticeUnreadPeopleView> {
    public NoticeUnreadPeoplePresenter(NoticeUnreadPeopleView view) {
        attachView(view);
    }

    public void getNoticeUnreadList(long id, int type, int current, int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("specifieType", type);
        map.put("current", current);
        map.put("size", size);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mvpView.showLoading();
        addSubscription(dingApiStores.noticeUnreadList(body), new ApiCallback<NoticeUnreadPeopleBean>() {
            @Override
            public void onSuccess(NoticeUnreadPeopleBean model) {
                mvpView.getUnreadPeopleList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getUnreadPeopleFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
