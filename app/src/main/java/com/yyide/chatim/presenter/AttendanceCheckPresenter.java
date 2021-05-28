package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AttendanceCheckView;
import com.yyide.chatim.view.AttendanceView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class AttendanceCheckPresenter extends BasePresenter<AttendanceCheckView> {
    public AttendanceCheckPresenter(AttendanceCheckView view) {
        attachView(view);
    }

    public void attendance(String classId) {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);//班级ID
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.teacherAttendance(body), new ApiCallback<AttendanceCheckRsp>() {
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
