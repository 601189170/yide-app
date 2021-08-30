package com.yyide.chatim.net;

import android.text.TextUtils;

import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.utils.LogUtil;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.adapter.rxjava3.HttpException;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public abstract class ApiCallback<M> implements Observer<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();
    
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        String msg;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            msg = httpException.getMessage();
            LogUtil.d("code=" + code);
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 500 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
        } else {
            msg = e.getMessage();
        }
        if (!BaseApplication.isNetworkAvailable(BaseApplication.getInstance())) {
            msg = "请检查网络连接";
        }
        if (!TextUtils.isEmpty(msg)) {
            onFailure(msg);
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        try {
            onSuccess(model);
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
//        onDisposable(d);
    }
}
