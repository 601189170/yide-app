package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.kotlin.network.base.BaseResponse;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.model.sitetable.SiteTableRsp;
import com.yyide.chatim.model.table.ClassInfoBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.ClassTableView;

import java.util.HashMap;
import java.util.List;

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
        addSubscription(dingApiStores.listAllBySchoolId(), new ApiCallback<BaseResponse<List<ClassInfoBean>>>() {
            @Override
            public void onSuccess(BaseResponse<List<ClassInfoBean>> model) {
                if (model.getCode() == 0) {
                    mvpView.listAllBySchoolId(model.getData());
                }else {
                    mvpView.listAllBySchoolIdFail(model.getMessage());
                }
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

    public void listTimeDataByApp(String classId,String weekTime) {
        mvpView.showLoading();
//        TableJSON info = new TableJSON(classid);
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type","1");
        hashMap.put("typeId",classId);
        if (weekTime != null) {
            hashMap.put("weekTime", weekTime);
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(hashMap));
        addSubscription(dingApiStores.listTimeDataByApp(body), new ApiCallback<SiteTableRsp>() {
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


    public void getWeekTime(String classId,String weekTime,String teacherIds,String subjectId) {
        mvpView.showLoading();
//        TableJSON info = new TableJSON(classid);
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type","1");
        hashMap.put("typeId",classId);
        hashMap.put("teacherIds",teacherIds);
        hashMap.put("subjectId",subjectId);
        if (weekTime != null) {
            hashMap.put("weekTime", weekTime);
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(hashMap));
        addSubscription(dingApiStores.getWeekTime(body), new ApiCallback<SiteTableRsp>() {
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
