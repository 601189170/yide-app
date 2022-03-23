package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.HomeFragmentView;
import com.yyide.chatim.view.TodoFragmentView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class TodoFragmentPresenter extends BasePresenter<TodoFragmentView> {

    public TodoFragmentPresenter(TodoFragmentView view) {
        attachView(view);
    }

    public void getMessageTransaction(int current, int size, String status) {
        HashMap<String, Object> map = new HashMap();
        map.put("pageNo", current);
        map.put("pageSize", size);
        map.put("status", status);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getMessageTransaction(body), new ApiCallback<TodoRsp>() {
            @Override
            public void onSuccess(TodoRsp model) {
                if (mvpView != null) {
                    mvpView.getMyNoticePageSuccess(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (mvpView != null) {
                    mvpView.getMyNoticePageFail(msg);
                }
            }

            @Override
            public void onFinish() {

            }
        });

    }

}
