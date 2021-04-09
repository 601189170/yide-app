package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AppView;
import com.yyide.chatim.view.BookSearchView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class BookSearchPresenter extends BasePresenter<BookSearchView> {
    public BookSearchPresenter(BookSearchView view) {
        attachView(view);
    }

    public void getMyAppList(){
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
        mvpView.showLoading();
        addSubscription(dingApiStores.getMyApp(body), new ApiCallback() {
            @Override
            public void onSuccess(Object model) {
                mvpView.hideLoading();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
