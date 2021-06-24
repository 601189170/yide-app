package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoticeMyReleasedView;
import com.yyide.chatim.view.NoticeReceivedView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class NoticeMyReleasePresenter extends BasePresenter<NoticeMyReleasedView> {
    public NoticeMyReleasePresenter(NoticeMyReleasedView view) {
        attachView(view);
    }

    public void getMyNoticeList(int current, int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("current", current);
        map.put("size", size);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mvpView.showLoading();
        addSubscription(dingApiStores.myNoticeList(body), new ApiCallback<NoticeItemBean>() {
            @Override
            public void onSuccess(NoticeItemBean model) {
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
