package com.yyide.chatim.presenter.leave;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.activity.notice.view.NoticeScopeView;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AddressBookRsp;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.DepartmentScopeRsp2;
import com.yyide.chatim.model.StudentScopeRsp;
import com.yyide.chatim.model.UniversityScopeRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.leave.AddressBookView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class AddressBookPresenter extends BasePresenter<AddressBookView> {
    private static final String TAG = AddressBookPresenter.class.getSimpleName();
    public AddressBookPresenter(AddressBookView view) {
        attachView(view);
    }

    public void queryDepartmentOverrideList(){
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.queryOrganizationStructure(body),new ApiCallback<DepartmentScopeRsp2>(){
            @Override
            public void onSuccess(DepartmentScopeRsp2 model) {
                mvpView.getDepartmentListSuccess2(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDepartmentListFail2(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void queryDeptMemberByDeptId(long deptId,int level){
        Log.e(TAG, "queryDeptMemberByDeptId: deptId="+deptId+",level="+level);
        HashMap<String, Object> map = new HashMap<>();
        map.put("deptId", deptId);
        map.put("level", level);
        mvpView.showLoading();
        addSubscription(dingApiStores.queryDeptMemberByDeptId(map), new ApiCallback<AddressBookRsp>() {
            @Override
            public void onSuccess(AddressBookRsp model) {
                mvpView.getDeptMemberListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDeptMemberListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
