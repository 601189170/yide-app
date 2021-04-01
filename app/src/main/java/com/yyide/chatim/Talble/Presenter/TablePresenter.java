package com.yyide.chatim.Talble.Presenter;



import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.TableJSON;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.Talble.View.listTimeDataByAppView;

import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class TablePresenter extends BasePresenter<listTimeDataByAppView> {

    public TablePresenter(listTimeDataByAppView view) {
        attachView(view);
    }




    public void listTimeDataByApp(int classid) {
        mvpView.showLoading();
        TableJSON info=new TableJSON(classid);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(info));
        addSubscription(dingApiStores.listTimeDataByApp(body), new ApiCallback<listTimeDataByAppRsp>() {
            @Override
            public void onSuccess(listTimeDataByAppRsp model) {
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






}
