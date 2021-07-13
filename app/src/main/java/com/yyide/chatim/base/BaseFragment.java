package com.yyide.chatim.base;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.net.DingApiStores;
import com.yyide.chatim.utils.LoadingTools;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class BaseFragment extends Fragment {
    public Activity mActivity;
    private LoadingTools loadingTools;
    protected DingApiStores mDingApiStores;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        loadingTools = new LoadingTools(getActivity());
        mDingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        onUnsubscribe();
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }

    public void showProgressDialog2() {
        if (loadingTools != null && !getActivity().isFinishing()) {
            loadingTools.showLoading();
        }
    }

    public void dismissProgressDialog2() {
        if (loadingTools != null) {
            loadingTools.closeLoading();
        }
    }

}
