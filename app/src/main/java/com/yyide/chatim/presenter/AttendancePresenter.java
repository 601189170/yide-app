package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AttendanceView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class AttendancePresenter extends BasePresenter<AttendanceView> {
    public AttendancePresenter(AttendanceView view) {
        attachView(view);
    }

    public void homeAttendance(String classId, String name, String type) {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);//班级ID
        map.put("name", name);//考勤名
        map.put("type", type);//节次
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.homeAttendance(body), new ApiCallback<HomeAttendanceRsp>() {
            @Override
            public void onSuccess(HomeAttendanceRsp model) {
                mvpView.getHomeAttendanceListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getHomeAttendanceFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
