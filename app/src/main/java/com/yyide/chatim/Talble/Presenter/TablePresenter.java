package com.yyide.chatim.Talble.Presenter;


import com.yyide.chatim.Talble.View.listTimeDataByAppView;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.net.ApiCallback;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class TablePresenter extends BasePresenter<listTimeDataByAppView> {

    public TablePresenter(listTimeDataByAppView view) {
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

}
