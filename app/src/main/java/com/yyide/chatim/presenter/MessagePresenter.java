package com.yyide.chatim.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.MessageNumberRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.MessageView;

import java.util.HashMap;

public class MessagePresenter extends BasePresenter<MessageView> {
    public MessagePresenter(MessageView view) {
        attachView(view);
    }

    public void getMessageNumber() {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap();
        map.put("current", 1);
        map.put("size", 5);
        map.put("isOperation", 0);
        addSubscription(dingApiStores.getMessageTransaction(map), new ApiCallback<TodoRsp>() {
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
