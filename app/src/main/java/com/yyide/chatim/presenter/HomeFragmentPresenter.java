package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.HomeFragmentView;
import com.yyide.chatim.view.MainView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class HomeFragmentPresenter extends BasePresenter<HomeFragmentView> {

    public HomeFragmentPresenter(HomeFragmentView view) {
        attachView(view);
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

    public void getHomeNotice() {
        mvpView.showLoading();
        addSubscription(dingApiStores.getIndexMyNotice(), new ApiCallback<NoticeHomeRsp>() {
            @Override
            public void onSuccess(NoticeHomeRsp model) {
                mvpView.getIndexMyNotice(model);
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



}
