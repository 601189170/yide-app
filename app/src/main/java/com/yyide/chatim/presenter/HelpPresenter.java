package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.HelpView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HelpPresenter extends BasePresenter<HelpView> {
    public HelpPresenter(HelpView view) {
        attachView(view);
    }

    public void getHelpList() {
        Map<String, Integer> map = new HashMap<>();
        map.put("size", 10);
        map.put("current", 1);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
//        mvpView.showLoading();
        addSubscription(dingApiStores.getHelpList(body), new ApiCallback<HelpItemRep>() {
            @Override
            public void onSuccess(HelpItemRep model) {
                if (model.getCode() == 200) {
                    mvpView.getHelpListSuccess(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getHelpListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

//    public void getAppList() {
////        {"size": 10,"current": 1}
//        Map<String, Object> map = new HashMap<>();
//        map.put("size", 100);
//        map.put("current", 1);
//        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
//        mvpView.showLoading();
//        addSubscription(dingApiStores.getAppList(body), new ApiCallback<AppItemBean>() {
//            @Override
//            public void onSuccess(AppItemBean model) {
//                mvpView.getAppListSuccess(model);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mvpView.getAppListFail(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                mvpView.hideLoading();
//            }
//        });
//    }
}
