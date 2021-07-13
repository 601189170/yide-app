package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.ScanLoginView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者：LRZ 2021年4月6日
 * DESC：扫码登录
 */
public class ScanLoginPresenter extends BasePresenter<ScanLoginView> {

    public ScanLoginPresenter(ScanLoginView view) {
        attachView(view);
    }

    public void scanLogin(String loginId) {
        Map<String, String> map = new HashMap<>();
        map.put("loginId", loginId);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.scanCodeLogin(body), new ApiCallback() {
            @Override
            public void onSuccess(Object model) {

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
