package com.yyide.chatim.presenter;


import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NewsDetailsEntity;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NewsDetailsView;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class NewsDetailsPresenter extends BasePresenter<NewsDetailsView> {

    public NewsDetailsPresenter(NewsDetailsView view) {
        attachView(view);
    }

    public void getDetailNews(String id) {
        mvpView.showLoading();
        addSubscription(zhihuApiStores.getDetailNews(id), new ApiCallback<NewsDetailsEntity>() {
            @Override
            public void onSuccess(NewsDetailsEntity model) {
                mvpView.getNewsDetailsSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getNewsDetailsFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
