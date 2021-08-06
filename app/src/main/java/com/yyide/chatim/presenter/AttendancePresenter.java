package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AttendanceCheckView;
import com.yyide.chatim.view.AttendanceView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class AttendancePresenter extends BasePresenter<AttendanceCheckView> {
    public AttendancePresenter(AttendanceCheckView view) {
        attachView(view);
    }

    public void homeAttendance(String classId, String studentId) {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);//班级ID
        map.put("studentId", studentId);//学生ID
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.viewAttendance(body), new ApiCallback<AttendanceCheckRsp>() {
            @Override
            public void onSuccess(AttendanceCheckRsp model) {
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
