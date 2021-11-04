package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AttendanceCheckView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class AttendanceTeacherPresenter extends BasePresenter<AttendanceCheckView> {
    public AttendanceTeacherPresenter(AttendanceCheckView view) {
        attachView(view);
    }

    public void attendance(String classId, String serverId, String type) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);//班级ID
        map.put("serverId", classId);//事件ID
        map.put("type", classId);//类型
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.teacherAttendance(body), new ApiCallback<AttendanceRsp>() {
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
