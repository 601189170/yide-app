package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.NoticeItemBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.NoticeReceivedView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class NoticeReceivedPresenter extends BasePresenter<NoticeReceivedView> {
    public NoticeReceivedPresenter(NoticeReceivedView view) {
        attachView(view);
    }

    public void getReceiverNoticeList(String startDate, String endDate, int current, int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);//开始时间（格式：yyyy-MM-dd HH:mm:ss，如2021-06-21 00:00:00）
        map.put("endDate", endDate);//开始时间（格式：yyyy-MM-dd HH:mm:ss，如2021-06-21 00:00:00）
        map.put("current", current);
        map.put("size", size);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mvpView.showLoading();
        addSubscription(dingApiStores.myReceiverNoticeList(body), new ApiCallback<NoticeItemBean>() {
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
