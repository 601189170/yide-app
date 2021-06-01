package com.yyide.chatim.presenter;


import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.HomeFragmentView;

import java.util.HashMap;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class HomeFragmentPresenter extends BasePresenter<HomeFragmentView> {

    public HomeFragmentPresenter(HomeFragmentView view) {
        attachView(view);
    }

    public void getUserSchool() {
//        mvpView.showLoading();
        addSubscription(dingApiStores.getUserSchool(), new ApiCallback<GetUserSchoolRsp>() {
            @Override
            public void onSuccess(GetUserSchoolRsp model) {
                mvpView.getUserSchool(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getUserSchoolDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getHomeNotice() {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap();
        map.put("current", 1);
        map.put("size", 5);
        map.put("isOperation", 0);
        addSubscription(dingApiStores.getMessageTransaction(map), new ApiCallback<TodoRsp>() {
            @Override
            public void onSuccess(TodoRsp model) {
                mvpView.getIndexMyNotice(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getUserSchoolDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
