package com.yyide.chatim.presenter.leave;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.StaffAskLeaveView;
import com.yyide.chatim.view.leave.StudentAskLeaveView;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class StudentAskLeavePresenter extends BasePresenter<StudentAskLeaveView> {
    public StudentAskLeavePresenter(StudentAskLeaveView view) {
        attachView(view);
    }

    public void getApprover(String classIdOrdeptId) {
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
     * 查询请假原因提示
     * @param type
     */
    public void queryLeavePhraseList(int type){
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

    public void addStudentLeave(String startTime, String endTime, String leaveReason, String reason,
                                String classId, String studentId,String studentUserId,String className, List<Long> carbonCopyPeopleIds) {
        final HashMap<String, Object> map = new HashMap<>(8);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("leaveReason", leaveReason);
        map.put("reason", reason);
        map.put("classId", classId);
        map.put("studentId", studentId);
        map.put("studentUserId", studentUserId);
        map.put("className", className);
        map.put("carbonCopyPeopleId", carbonCopyPeopleIds);
        mvpView.showLoading();
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        Log.e("StaffAskLeavePresenter", "addStudentLeave: "+JSON.toJSONString(map));
        addSubscription(dingApiStores.addStudentLeave(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                mvpView.addStudentLeave(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addStudentLeaveFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }
}
