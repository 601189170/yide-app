package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetAppVersionResponse;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.WeeklyDateBean;
import com.yyide.chatim.model.WeeklyDescBean;
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
        map.put("terminal", "2");//terminal【0：全部，1：IOS，2：Android，3：WEB，4：班牌】
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.updateVersion(body), new ApiCallback<GetAppVersionResponse>() {
            @Override
            public void onSuccess(GetAppVersionResponse model) {
                mvpView.getVersionInfo(model);
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

    public void copywriter() {
        addSubscription(dingApiStores.copywriter(), new ApiCallback<WeeklyDescBean>() {
            @Override
            public void onSuccess(WeeklyDescBean model) {
                mvpView.getCopywriter(model);
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

    public void getWeeklyDate() {
        addSubscription(dingApiStores.getWeeklyDate(), new ApiCallback<WeeklyDateBean>() {
            @Override
            public void onSuccess(WeeklyDateBean model) {
                mvpView.getWeeklyDate(model);
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
