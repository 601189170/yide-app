package com.yyide.chatim.activity;

import android.os.Bundle;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.presenter.BookSearchPresenter;
import com.yyide.chatim.view.BookSearchView;

public class BookSearchActivity extends BaseMvpActivity<BookSearchPresenter> implements BookSearchView {

    @Override
    public int getContentViewID() {
        return R.layout.activity_book_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BookSearchPresenter createPresenter() {
        return new BookSearchPresenter(this);
    }

    @Override
    public void getMyAppListSuccess() {

    }

    @Override
    public void getMyAppFail(String msg) {

    }

    @Override
    public void showError() {

    }
}