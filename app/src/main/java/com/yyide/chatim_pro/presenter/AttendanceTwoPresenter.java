package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.AttendanceRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.AttendanceCheckView;

import okhttp3.RequestBody;

public class AttendanceTwoPresenter extends BasePresenter<AttendanceCheckView> {
    public AttendanceTwoPresenter(AttendanceCheckView view) {
        attachView(view);
    }

    public void attendance(AttendanceRsp.DataBean.AttendanceListBean attendanceRequestBean) {
        mvpView.showLoading();
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(attendanceRequestBean));
        addSubscription(dingApiStores.schoolTwoAttendance(body), new ApiCallback<AttendanceRsp>() {
            @Override
            public void onSuccess(AttendanceRsp model) {
                mvpView.getAttendanceSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getAttendanceFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
