package com.yyide.chatim.base;


import android.app.Activity;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;


import com.yyide.chatim.utils.LoadingTools;

import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class BaseFragment extends Fragment {
    public Activity mActivity;

    private SweetAlertDialog pd;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        pd = new LoadingTools().pd(mActivity);
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
        if (pd == null)
            pd = new LoadingTools().pd(mActivity);
        if (pd != null)
            pd.show();
    }

    public void dismissProgressDialog2() {
        if (pd != null && pd.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            pd.dismiss();
        }
    }

}
