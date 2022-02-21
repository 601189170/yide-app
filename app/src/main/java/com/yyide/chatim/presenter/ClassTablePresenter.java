package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.model.TableJSON;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.model.sitetable.SiteTableRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.ClassTableView;

import java.util.HashMap;

import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class ClassTablePresenter extends BasePresenter<ClassTableView> {

    public ClassTablePresenter(ClassTableView view) {
        attachView(view);
    }

    public void listAllBySchoolId() {

        mvpView.showLoading();
        addSubscription(dingApiStores.listAllBySchoolId(), new ApiCallback<listAllBySchoolIdRsp>() {
            @Override
            public void onSuccess(listAllBySchoolIdRsp model) {
//                mvpView.hideLoading();
                mvpView.listAllBySchoolId(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.listAllBySchoolIdFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void listTimeDataByApp(String classid) {
        mvpView.showLoading();
//        TableJSON info = new TableJSON(classid);
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type","1");
        hashMap.put("typeId",classid);
        //hashMap.put("weekTime",weekTime);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(hashMap));
        addSubscription(dingApiStores2.listTimeDataByApp(body), new ApiCallback<SiteTableRsp>() {
            @Override
            public void onSuccess(SiteTableRsp model) {
                mvpView.listTimeDataByApp(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.listTimeDataByAppFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    //大学组织结构
    public void selectClassByAllSchool() {
        mvpView.showLoading();
        addSubscription(dingApiStores.selectClassByAllSchool(), new ApiCallback<SelectTableClassesRsp>() {
            @Override
            public void onSuccess(SelectTableClassesRsp model) {
                mvpView.selectTableClassListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.selectTableClassListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    //小初高
    public void selectListBySchoolAll() {
        mvpView.showLoading();
        addSubscription(dingApiStores.selectListBySchoolAll(), new ApiCallback<SelectTableClassesRsp>() {
            @Override
            public void onSuccess(SelectTableClassesRsp model) {
                mvpView.selectTableClassListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.selectTableClassListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
