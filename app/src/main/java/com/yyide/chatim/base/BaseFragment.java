package com.yyide.chatim.base;


import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.net.DingApiStores;
import com.yyide.chatim.utils.LoadingTools;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class BaseFragment extends Fragment {
    public Activity mActivity;
    private LoadingTools loadingTools;
    protected DingApiStores mDingApiStores;
    private Unbinder unbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();
        loadingTools = new LoadingTools(getActivity());
        mDingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismiss();
        unbinder.unbind();
    }

    public void loading() {
        if (loadingTools != null && !getActivity().isFinishing()) {
            loadingTools.showLoading();
        }
    }

    public void dismiss() {
        if (loadingTools != null) {
            loadingTools.closeLoading();
        }
    }


}
