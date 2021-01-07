package com.yyide.chatim.presenter;


import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NewsEntity;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NewsView;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class NewsPresenter extends BasePresenter<NewsView> {

    public NewsPresenter(NewsView view) {
        attachView(view);
    }

    public void getVideoList() {
        mvpView.showLoading();
        addSubscription(zhihuApiStores.getLatestNews(), new ApiCallback<NewsEntity>() {
            @Override
            public void onSuccess(NewsEntity model) {
                mvpView.getNewsSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getNewsFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getBeforetNews(String time) {
        mvpView.showLoading();
        addSubscription(zhihuApiStores.getBeforetNews(time), new ApiCallback<NewsEntity>() {
            @Override
            public void onSuccess(NewsEntity model) {
                mvpView.getNewsSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getNewsFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
