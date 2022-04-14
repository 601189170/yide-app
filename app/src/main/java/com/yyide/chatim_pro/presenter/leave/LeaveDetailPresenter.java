package com.yyide.chatim_pro.presenter.leave;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.LeaveDetailRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.leave.LeaveDetailView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class LeaveDetailPresenter extends BasePresenter<LeaveDetailView> {
    public LeaveDetailPresenter(LeaveDetailView view) {
        attachView(view);
    }

    public void queryLeaveDetailsById(String id) {
        mvpView.showLoading();
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        addSubscription(dingApiStores.queryLeaveDetailsById(map), new ApiCallback<LeaveDetailRsp>() {
            @Override
            public void onSuccess(LeaveDetailRsp model) {
                mvpView.leaveDetail(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.leaveDetailFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void ondoApplyLeave(String id, String reason) {
        mvpView.showLoading();
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("processInstanceId", id);
        map.put("deleteReason", reason);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.ondoApplyLeave(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.repealResult(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.repealFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * 请假审批确认
     *
     * @param taskId  long
     * @param outcome 0 同意 2 拒绝
     */
    public void processExaminationApproval(String taskId, int outcome, String reason) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("outcome", outcome);
        map.put("comment", reason);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.processExaminationApproval(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.processApproval(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.processApprovalFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * 请假回退
     *
     * @param taskId
     */
    public void backLeave(String taskId) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("taskId", taskId);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.backLeave(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.leaveBack(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.repealFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
