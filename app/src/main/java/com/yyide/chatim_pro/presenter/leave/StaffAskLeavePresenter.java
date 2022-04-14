package com.yyide.chatim_pro.presenter.leave;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.LeaveApprovalBean;
import com.yyide.chatim_pro.model.LeavePhraseRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.leave.StaffAskLeaveView;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class StaffAskLeavePresenter extends BasePresenter<StaffAskLeaveView> {
    public StaffAskLeavePresenter(StaffAskLeaveView view) {
        attachView(view);
    }

    public void getApprover(String startDate, String endDate, String classesId) {
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("classesId", classesId);
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

    public void getClassList() {
        mvpView.showLoading();
        addSubscription(dingApiStores.getDeptOrClass(), new ApiCallback<LeaveApprovalBean>() {
            @Override
            public void onSuccess(LeaveApprovalBean model) {
                mvpView.getClass(model);
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
     * @param sponsorType
     * @param dept
     * @param approverList
     */
    public void addLeave(LeaveApprovalBean.LeaveRequestBean requestBean, boolean isBack, String procId, String sponsorType, String dept, String deptName, List<LeaveApprovalBean.RequestParam> approverList, String ccIds) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("jsonData", JSON.toJSONString(requestBean));
        map.put("procId", procId);
        map.put("dept", dept);
        map.put("isBack", isBack);
        map.put("deptName", deptName);
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
