package com.yyide.chatim.presenter;



import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ListScheduleRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.VideoEntity;
import com.yyide.chatim.model.addUserEquipmentInfoRsp;
import com.yyide.chatim.model.getUserSigRsp;
import com.yyide.chatim.model.listTimeDataRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.MainView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.cache.Sp;
import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        attachView(view);
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
                mvpView.getDataFail(msg);
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
                mvpView.UserLogoutDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getUserSchool() {
        mvpView.showLoading();
        addSubscription(dingApiStores.getUserSchool(), new ApiCallback<GetUserSchoolRsp>() {
            @Override
            public void onSuccess(GetUserSchoolRsp model) {
                mvpView.getUserSchool(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getUserSchoolDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


    public void SelectSchByTeaid() {
        mvpView.showLoading();
        addSubscription(dingApiStores.selectSchByTeaid(), new ApiCallback<SelectSchByTeaidRsp>() {
            @Override
            public void onSuccess(SelectSchByTeaidRsp model) {
                mvpView.selectSchByTeaid(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.selectSchByTeaidDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


    public void listAllScheduleByTeacherId() {
        mvpView.showLoading();
        addSubscription(dingApiStores.listAllScheduleByTeacherId(), new ApiCallback<ListAllScheduleByTeacherIdRsp>() {
            @Override
            public void onSuccess(ListAllScheduleByTeacherIdRsp model) {
                mvpView.listAllScheduleByTeacherId(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.listAllScheduleByTeacherIdDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void addUserEquipmentInfo(int userId,String registrationId,String alias,String equipmentType) {
        mvpView.showLoading();
        Map<String,String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));
        map.put("registrationId", registrationId);
        map.put("alias", alias);
        map.put("equipmentType", equipmentType);
        String json = JSON.toJSONString(map,true);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(json));
        addSubscription(dingApiStores.addUserEquipmentInfo(body), new ApiCallback<addUserEquipmentInfoRsp>() {
            @Override
            public void onSuccess(addUserEquipmentInfoRsp model) {
                mvpView.addUserEquipmentInfo(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addUserEquipmentInfoFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
