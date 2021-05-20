package com.yyide.chatim.presenter.leave;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.StaffAskLeaveView;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class StaffAskLeavePresenter extends BasePresenter<StaffAskLeaveView> {
    public StaffAskLeavePresenter(StaffAskLeaveView view) {
        attachView(view);
    }

    public void queryDeptInfoByTeacherId() {
        mvpView.showLoading();
        addSubscription(dingApiStores.queryDeptInfoByTeacherId(), new ApiCallback<LeaveDeptRsp>() {
            @Override
            public void onSuccess(LeaveDeptRsp model) {
                mvpView.leaveDept(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.leaveDeptFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getApprover(long classIdOrdeptId) {
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("classIdOrdeptId", classIdOrdeptId);
        mvpView.showLoading();
        addSubscription(dingApiStores.getApprover(map), new ApiCallback<ApproverRsp>() {
            @Override
            public void onSuccess(ApproverRsp model) {
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
     * 老师请假
     * @param startTime
     * @param endTime
     * @param leaveReason
     * @param reason
     * @param deptId
     * @param deptName
     * @param carbonCopyPeopleIds
     */
    public void addTeacherLeave(String startTime, String endTime, String leaveReason, String reason,
                                long deptId, String deptName, List<Long> carbonCopyPeopleIds) {
        final HashMap<String, Object> map = new HashMap<>(7);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("leaveReason", leaveReason);
        map.put("reason", reason);
        map.put("deptId", deptId);
        map.put("deptName", deptName);
        map.put("carbonCopyPeopleId", carbonCopyPeopleIds);
        mvpView.showLoading();
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        Log.e("StaffAskLeavePresenter", "addTeacherLeave: "+JSON.toJSONString(map));
        addSubscription(dingApiStores.addTeacherLeave(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.addTeacherLeave(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addTeacherLeaveFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }
}
