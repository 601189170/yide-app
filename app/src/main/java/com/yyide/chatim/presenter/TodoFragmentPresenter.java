package com.yyide.chatim.presenter;


import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.HomeFragmentView;
import com.yyide.chatim.view.TodoFragmentView;

import java.util.HashMap;
import java.util.Map;

public class TodoFragmentPresenter extends BasePresenter<TodoFragmentView> {

    public TodoFragmentPresenter(TodoFragmentView view) {
        attachView(view);
    }

    public void getMyNoticePage(int current, int size, String status) {
        HashMap<String, Object> map = new HashMap();
        map.put("current", current);
        map.put("size", size);
        map.put("status", status);
        addSubscription(dingApiStores.getMyNoticePage(map), new ApiCallback<TodoRsp>() {
            @Override
            public void onSuccess(TodoRsp model) {
                mvpView.getMyNoticePageSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getMyNoticePageFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });

    }

}
