package com.yyide.chatim_pro.Talble.Presenter;


import com.yyide.chatim_pro.Talble.View.listTimeDataByAppView;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.HomeTimeTable;
import com.yyide.chatim_pro.model.SelectSchByTeaidRsp;
import com.yyide.chatim_pro.net.ApiCallback;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class TablePresenter extends BasePresenter<listTimeDataByAppView> {

    public TablePresenter(listTimeDataByAppView view) {
        attachView(view);
    }

    public void SelectSchByTeaid() {
//        mvpView.showLoading();
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("classesId",classesId);
//        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.selectSchByTeaid(), new ApiCallback<SelectSchByTeaidRsp>() {
            @Override
            public void onSuccess(SelectSchByTeaidRsp model) {
                mvpView.SelectSchByTeaid(model);
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
//        mvpView.showLoading();
        addSubscription(dingApiStores.selectClassInfoByClassId(classId), new ApiCallback<SelectSchByTeaidRsp>() {
            @Override
            public void onSuccess(SelectSchByTeaidRsp model) {
                mvpView.SelectSchByTeaid(model);
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

    public void getHomeTable() {
        addSubscription(dingApiStores.getHomeTimeTable(), new ApiCallback<HomeTimeTable>() {
            @Override
            public void onSuccess(HomeTimeTable model) {
                mvpView.SelectHomeTimeTbale(model);
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
