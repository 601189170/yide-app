package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.AttendanceCheckRsp;
import com.yyide.chatim_pro.model.AttendanceRsp;
import com.yyide.chatim_pro.model.AttendanceSchoolGradeRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.SchoolGradeView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class SchoolGradePresenter extends BasePresenter<SchoolGradeView> {
    public SchoolGradePresenter(SchoolGradeView view) {
        attachView(view);
    }

    public void getMyAppList(AttendanceRsp.DataBean.AttendanceListBean request) {
        mvpView.showLoading();
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(request));
        addSubscription(dingApiStores.schoolClassesAttendance(body), new ApiCallback<AttendanceRsp>() {
            @Override
            public void onSuccess(AttendanceRsp model) {
                mvpView.getGradeListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
