package com.yyide.chatim.presenter.leave;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.LeaveListRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.AskForLeaveListView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class AskForLeaveListPresenter extends BasePresenter<AskForLeaveListView> {
    public AskForLeaveListPresenter(AskForLeaveListView view) {
        attachView(view);
    }

    /**
     * 请假列表
     *
     * @param pageNo
     * @param pageSize
     */
    public void getAskLeaveRecord(int pageNo, int pageSize) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getAskLeaveRecord(body), new ApiCallback<LeaveListRsp>() {
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
