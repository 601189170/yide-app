package com.yyide.chatim_pro.activity.notice.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.activity.notice.view.NoticeScopeView;
import com.yyide.chatim_pro.model.DepartmentScopeRsp;
import com.yyide.chatim_pro.model.StudentScopeRsp;
import com.yyide.chatim_pro.model.UniversityScopeRsp;
import com.yyide.chatim_pro.net.ApiCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class NoticeScopePresenter extends BasePresenter<NoticeScopeView> {
    public NoticeScopePresenter(NoticeScopeView view) {
        attachView(view);
    }

    /**
     * 获取学段
     */
    public void getSectionList(){
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getSectionList(body),new ApiCallback<StudentScopeRsp>(){

            @Override
            public void onSuccess(StudentScopeRsp model) {
                mvpView.getStudentScopeSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getStudentScopeFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * 查询大学学段
     */
    public void queryDepartmentClassList(){
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        Log.e("tag", "queryDepartmentClassList: "+JSON.toJSONString(map));
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.queryDepartmentClassList(body),new ApiCallback<UniversityScopeRsp>(){

            @Override
            public void onSuccess(UniversityScopeRsp model) {
                mvpView.getUniversityListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getUniversityListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getDepartmentList(){
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getDepartmentList(body),new ApiCallback<DepartmentScopeRsp>(){
            @Override
            public void onSuccess(DepartmentScopeRsp model) {
                mvpView.getDepartmentListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDepartmentListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
