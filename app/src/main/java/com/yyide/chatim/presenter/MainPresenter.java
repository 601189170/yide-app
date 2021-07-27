package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.MainView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        attachView(view);
    }

    public void getVersionInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("terminal", "Android");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.updateVersion(body), new ApiCallback<SelectUserRsp>() {
            @Override
            public void onSuccess(SelectUserRsp model) {
                mvpView.getData(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getselectUser() {
        mvpView.showLoading();
        addSubscription(dingApiStores.getSelectUser(), new ApiCallback<SelectUserRsp>() {
            @Override
            public void onSuccess(SelectUserRsp model) {
                mvpView.getData(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


    public void ToUserLogout() {
        mvpView.showLoading();
        addSubscription(dingApiStores.Logout(), new ApiCallback<UserLogoutRsp>() {
            @Override
            public void onSuccess(UserLogoutRsp model) {
                mvpView.UserLogoutData(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

//    public void getUserSchool() {
//        mvpView.showLoading();
//        addSubscription(dingApiStores.getUserSchool(), new ApiCallback<GetUserSchoolRsp>() {
//            @Override
//            public void onSuccess(GetUserSchoolRsp model) {
//                mvpView.getUserSchool(model);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mvpView.getUserSchoolDataFail(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                mvpView.hideLoading();
//            }
//        });
//    }


    public void listAllScheduleByTeacherId() {
        mvpView.showLoading();
        addSubscription(dingApiStores.listAllScheduleByTeacherId(), new ApiCallback<ListAllScheduleByTeacherIdRsp>() {
            @Override
            public void onSuccess(ListAllScheduleByTeacherIdRsp model) {
                mvpView.listAllScheduleByTeacherId(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void addUserEquipmentInfo(String registrationId, String alias, String equipmentType) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<String, String>();
        map.put("registrationId", registrationId);
        map.put("alias", alias);
        map.put("equipmentType", equipmentType);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.addUserEquipmentInfo(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.addUserEquipmentInfo(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


}
