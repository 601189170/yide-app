package com.yyide.chatim.presenter.leave;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveApprovalBean;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.StaffAskLeaveView;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class StaffAskLeavePresenter extends BasePresenter<StaffAskLeaveView> {
    public StaffAskLeavePresenter(StaffAskLeaveView view) {
        attachView(view);
    }

    public void getApprover(String startDate, String endDate) {
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        mvpView.showLoading();
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getApprover(body), new ApiCallback<LeaveApprovalBean>() {
            @Override
            public void onSuccess(LeaveApprovalBean model) {
                mvpView.approver(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.approverFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * 查询请假原因提示
     *
     * @param type
     */
    public void queryLeavePhraseList(int type) {
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("type", type);
        addSubscription(dingApiStores.queryLeavePhraseList(map), new ApiCallback<LeavePhraseRsp>() {
            @Override
            public void onSuccess(LeavePhraseRsp model) {
                mvpView.leavePhrase(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.leavePhraseFail(msg);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    /**
     * 老师请假
     *
     * @param startTime
     * @param endTime
     * @param sponsorType
     * @param reason
     * @param deptId
     * @param deptName
     * @param approverList
     */
    public void addLeave(String procId, String startTime, String endTime, String sponsorType, String reason,
                         String deptId, String deptName, String hours, List<LeaveApprovalBean.LeaveCommitBean> approverList, String ccIds) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("procId", procId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("reason", reason);
        map.put("deptId", deptId);
        map.put("dept", deptName);
        map.put("hours", hours);
        map.put("sponsorType", sponsorType);
        map.put("apprs", approverList);
        map.put("ccIds", ccIds);
        mvpView.showLoading();
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        Log.e("StaffAskLeavePresenter", "addTeacherLeave: " + JSON.toJSONString(map));
        addSubscription(dingApiStores.addLeave(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.addLeave(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addLeaveFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }
}
