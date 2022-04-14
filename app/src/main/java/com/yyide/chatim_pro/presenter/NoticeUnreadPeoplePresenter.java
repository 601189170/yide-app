package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.NoticeItemBean;
import com.yyide.chatim_pro.model.NoticeUnreadPeopleBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.NoticeReceivedView;
import com.yyide.chatim_pro.view.NoticeUnreadPeopleView;

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
