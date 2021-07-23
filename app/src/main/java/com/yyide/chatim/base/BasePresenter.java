package com.yyide.chatim.base;

import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.net.DingApiStores;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class BasePresenter<V> {
    protected V mvpView;
    protected DingApiStores dingApiStores;

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        dingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
    }

    public void detachView() {
        BaseCompositeDisposable.instance().onUnsubscribe();
        this.mvpView = null;
    }

    public void addSubscription(Observable observable, Observer subscriber) {
        BaseCompositeDisposable.instance().addSubscription(observable, subscriber);
    }
}
