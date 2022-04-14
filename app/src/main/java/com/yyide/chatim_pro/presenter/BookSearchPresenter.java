package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.activity.book.BookSearchActivity;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.BookRsp;
import com.yyide.chatim_pro.model.BookSearchRsp;
import com.yyide.chatim_pro.model.BookSearchRsp2;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.UserInfoRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.AppView;
import com.yyide.chatim_pro.view.BookSearchView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class BookSearchPresenter extends BasePresenter<BookSearchView> {
    public BookSearchPresenter(BookSearchView view) {
        attachView(view);
    }

    public void bookSearch(String name, String type,String queryType){
//        {
//            "type":1,
//                "name":"1"
//        }
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", type);
        hashMap.put("name",name);

        //试用范围 1 通讯录 2 闸机
        if (BookSearchActivity.FROM_GATE.equals(queryType)) {
            hashMap.put("queryType", 2);
        } else {
            hashMap.put("queryType", 1);
        }
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
