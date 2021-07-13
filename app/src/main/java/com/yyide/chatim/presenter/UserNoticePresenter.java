package com.yyide.chatim.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.UserMsgNoticeRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.UserNoticeView;

import java.util.HashMap;

public class UserNoticePresenter extends BasePresenter<UserNoticeView> {
    public UserNoticePresenter(UserNoticeView view) {
        attachView(view);
    }

    /**
     * 获取用户通知信息列表
     *
     * @param current
     * @param size
     */
    public void getUserNoticePage(int current, int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("current", current);
        map.put("size", size);
        addSubscription(dingApiStores.queryUserMessageNotice(map), new ApiCallback<UserMsgNoticeRsp>() {
            @Override
            public void onSuccess(UserMsgNoticeRsp model) {
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

    /**
     * 更新我的信息
     */
    public void updateMyNoticeDetails(long id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        addSubscription(dingApiStores.updateMyNoticeDetails(map), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.updateNoticeSuccess(model);
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
