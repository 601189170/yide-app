package com.yyide.chatim.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.MessageNumberRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.MessageView;

import java.util.HashMap;

public class MessagePresenter extends BasePresenter<MessageView> {
    public MessagePresenter(MessageView view) {
        attachView(view);
    }

    public void getMessageNumber() {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("isOperation", "2");//待办状态 1 已办 0待办 2全部
        addSubscription(dingApiStores.queryBocklogNuberByStatus(map), new ApiCallback<MessageNumberRsp>() {
            @Override
            public void onSuccess(MessageNumberRsp model) {
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
