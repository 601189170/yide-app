package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BookRsp;
import com.yyide.chatim.model.BookSearchRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.UserInfoRsp;
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

    public void bookSearch(String name, String type){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", type);
        hashMap.put("name",name);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(hashMap));
        mvpView.showLoading();
        addSubscription(dingApiStores.selectAllList(body), new ApiCallback<BookSearchRsp>() {
            @Override
            public void onSuccess(BookSearchRsp model) {
                mvpView.selectUserListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.selectUserListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
