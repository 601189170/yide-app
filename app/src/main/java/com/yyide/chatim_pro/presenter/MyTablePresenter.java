package com.yyide.chatim_pro.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.kotlin.network.base.BaseResponse;
import com.yyide.chatim_pro.model.SelectSchByTeaidRsp;
import com.yyide.chatim_pro.model.table.MyTableBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.MyTableView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class MyTablePresenter extends BasePresenter<MyTableView> {


    public MyTablePresenter(MyTableView view) {
        attachView(view);
    }

    public void SelectSchByTeaid() {
        mvpView.showLoading();
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("classesId",classesId);
//        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.selectSchByTeaid(), new ApiCallback<SelectSchByTeaidRsp>() {
            @Override
            public void onSuccess(SelectSchByTeaidRsp model) {
                //mvpView.SelectSchByTeaid(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.SelectSchByTeaidFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void selectClassInfoByClassId(String classId) {
        addSubscription(dingApiStores.selectClassInfoByClassId(classId), new ApiCallback<SelectSchByTeaidRsp>() {
            @Override
            public void onSuccess(SelectSchByTeaidRsp model) {
                //mvpView.SelectSchByTeaid(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.SelectSchByTeaidFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getMyTimeTable(String weekTime){
        mvpView.showLoading();
        addSubscription(dingApiStores.getMyTableData(weekTime), new ApiCallback<BaseResponse<MyTableBean>>() {
            @Override
            public void onSuccess(BaseResponse<MyTableBean> model) {
                if (model.getCode() == 0){
                    mvpView.SelectSchByTeaid(model.getData());
                }else {
                    mvpView.SelectSchByTeaidFail(model.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.SelectSchByTeaidFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
