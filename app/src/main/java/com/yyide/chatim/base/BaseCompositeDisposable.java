package com.yyide.chatim.base;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BaseCompositeDisposable {
    private static BaseCompositeDisposable mBaseCompositeDisposable;
    private CompositeDisposable mCompositeDisposable;

    public static synchronized BaseCompositeDisposable instance() {
        if (mBaseCompositeDisposable == null) {
            synchronized (BaseCompositeDisposable.class) {
                if (mBaseCompositeDisposable == null) {
                    mBaseCompositeDisposable = new BaseCompositeDisposable();
                }
            }
        }
        return mBaseCompositeDisposable;
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    public void addSubscription(Disposable mDisposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDisposable);
    }

    public void addSubscription(Observable<Object> observable, Observer<Object> subscriber) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
