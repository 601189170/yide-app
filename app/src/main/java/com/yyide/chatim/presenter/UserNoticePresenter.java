package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.UserNoticeRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.UserNoticeView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class UserNoticePresenter extends BasePresenter<UserNoticeView> {
    public UserNoticePresenter(UserNoticeView view) {
        attachView(view);
    }

    /**
     * 获取用户通知信息列表
     * @param type
     * @param current
     * @param size
     */
    public void getUserNoticePage(int type, int current, int size) {
        Map<String,Object> map = new HashMap<>();
        map.put("type", type);
        map.put("current", current);
        map.put("size", size);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getUserNoticePage(body), new ApiCallback<UserNoticeRsp>() {
            @Override
            public void onSuccess(UserNoticeRsp model) {
                mvpView.getUserNoticePageSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getUserNoticePageFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
