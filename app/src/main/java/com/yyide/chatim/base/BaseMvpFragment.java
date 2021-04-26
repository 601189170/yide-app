package com.yyide.chatim.base;


import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
        hideLoading();
    }

    public void showLoading() {
//        showProgressDialog();
        showProgressDialog2();
    }

    public void hideLoading() {
//        dismissProgressDialog();
        dismissProgressDialog2();
    }

    public void showError() {
//        dismissProgressDialog();
        dismissProgressDialog2();
        ToastUtils.showShort("出错啦！");
    }
}
