package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.MessageNumberRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.MessageView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class MessagePresenter extends BasePresenter<MessageView> {
    public MessagePresenter(MessageView view) {
        attachView(view);
    }

    public void getMessageNumber() {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap();
        map.put("current", 1);
        map.put("size", 5);
        map.put("status", 0);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getMessageTransaction(body), new ApiCallback<TodoRsp>() {
            @Override
            public void onSuccess(TodoRsp model) {
                mvpView.messageNumberSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.messageNumberFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
