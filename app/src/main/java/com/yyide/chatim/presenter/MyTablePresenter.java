package com.yyide.chatim.presenter;


import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.MyTableView;

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
