package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AttendanceCheckView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class AttendanceHomePresenter extends BasePresenter<AttendanceCheckView> {
    public AttendanceHomePresenter(AttendanceCheckView view) {
        attachView(view);
    }

    public void attendance(String classId) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);//班级ID
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.viewAttendance(body), new ApiCallback<AttendanceRsp>() {
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
