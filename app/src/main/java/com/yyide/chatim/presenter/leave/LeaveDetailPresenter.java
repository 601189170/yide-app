package com.yyide.chatim.presenter.leave;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.LeaveDetailView;

import java.util.HashMap;

public class LeaveDetailPresenter extends BasePresenter<LeaveDetailView> {
    public LeaveDetailPresenter(LeaveDetailView view) {
        attachView(view);
    }
    public void queryLeaveDetailsById(long id){
        mvpView.showLoading();
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("id",id);
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

    public void ondoApplyLeave(long id){
        mvpView.showLoading();
        final HashMap<String, Object> map = new HashMap<>(1);
        map.put("id",id);
        addSubscription(dingApiStores.ondoApplyLeave(map), new ApiCallback<BaseRsp>() {
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
     * @param id long
     * @param type 0 同意 1 拒绝
     */
    public void processExaminationApproval(long id,int type){
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("id",id);
        map.put("type",type);
        addSubscription(dingApiStores.processExaminationApproval(map), new ApiCallback<BaseRsp>() {
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
}
