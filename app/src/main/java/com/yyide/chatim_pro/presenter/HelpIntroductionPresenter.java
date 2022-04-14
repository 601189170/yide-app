package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.HelpItemRep;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.HelpIntroductionView;
import com.yyide.chatim_pro.view.HelpView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class HelpIntroductionPresenter extends BasePresenter<HelpIntroductionView> {
    public HelpIntroductionPresenter(HelpIntroductionView view) {
        attachView(view);
    }

    public void getHelpList(int pageSize, int pageNum) {
        Map<String, Integer> map = new HashMap<>();
        map.put("size", pageSize);
        map.put("current", pageNum);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getHelpIntroduction(body), new ApiCallback<HelpItemRep>() {
            @Override
            public void onSuccess(HelpItemRep model) {
                mvpView.getHelpListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getHelpListFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void getHelpAdvancedList(int pageSize, int pageNum) {
        Map<String, Integer> map = new HashMap<>();
        map.put("size", pageSize);
        map.put("current", pageNum);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getHelpAdvanced(body), new ApiCallback<HelpItemRep>() {
            @Override
            public void onSuccess(HelpItemRep model) {
                mvpView.getHelpListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getHelpListFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
