package com.yyide.chatim.activity.notice.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeScopeView;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.StudentScopeRsp;
import com.yyide.chatim.net.ApiCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class NoticeScopePresenter extends BasePresenter<NoticeScopeView> {
    public NoticeScopePresenter(NoticeScopeView view) {
        attachView(view);
    }

    /**
     * 获取学段
     * @param current
     * @param size
     */
    public void getSectionList(int current,int size){
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        map.put("current",current);
        map.put("size",size);
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

    public void getDepartmentList(int current,int size){
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        map.put("current",current);
        map.put("size",size);
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
