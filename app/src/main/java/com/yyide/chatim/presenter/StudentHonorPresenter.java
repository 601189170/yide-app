package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.StudentHonorRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AppAddView;
import com.yyide.chatim.view.StudentHonorView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class StudentHonorPresenter extends BasePresenter<StudentHonorView> {
    public StudentHonorPresenter(StudentHonorView view) {
        attachView(view);
    }

    public void getStudentHonorList(String classesId) {
//        mvpView.showLoading();
        Map map = new HashMap<>();
        map.put("classesId", classesId);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getStudentHonorList(body), new ApiCallback<StudentHonorRsp>() {
            @Override
            public void onSuccess(StudentHonorRsp model) {
                mvpView.getStudentHonorSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getStudentHonorFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
