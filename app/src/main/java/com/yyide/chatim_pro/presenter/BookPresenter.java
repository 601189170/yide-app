package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.BookRsp;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.BookView;
import com.yyide.chatim_pro.view.NoticeDetailView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class BookPresenter extends BasePresenter<BookView> {
    public BookPresenter(BookView view) {
        attachView(view);
    }

    public void getAddressBook(String type) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getAddressBook(body), new ApiCallback<BookRsp>() {
            @Override
            public void onSuccess(BookRsp model) {
                mvpView.getBookList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getBookListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
