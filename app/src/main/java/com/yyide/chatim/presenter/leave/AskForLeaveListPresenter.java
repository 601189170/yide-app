package com.yyide.chatim.presenter.leave;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.LeaveListRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.AskForLeaveListView;

import java.util.HashMap;
import java.util.Map;

public class AskForLeaveListPresenter extends BasePresenter<AskForLeaveListView> {
    public AskForLeaveListPresenter(AskForLeaveListView view) {
        attachView(view);
    }

    /**
     * 请假列表
     * @param current
     * @param size
     */
    public void getAskLeaveRecord(int current,int size){
        final HashMap<String, Object> map = new HashMap<>(2);
        map.put("size",size);
        map.put("current",current);
        addSubscription(dingApiStores.getAskLeaveRecord(map), new ApiCallback<LeaveListRsp>() {
            @Override
            public void onSuccess(LeaveListRsp model) {
                mvpView.leaveList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.leaveListFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
