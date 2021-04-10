package com.yyide.chatim.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.presenter.BookSearchPresenter;
import com.yyide.chatim.view.BookSearchView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public class BookSearchActivity extends BaseMvpActivity<BookSearchPresenter> implements BookSearchView {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.recyclerview_history)
    RecyclerView mRecyclerviewHistory;

    @BindView(R.id.search)
    EditText search;

    @Override
    public int getContentViewID() {
        return R.layout.activity_book_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSearchRecyclerView();
        initHistoryRecyclerView();
    }

    private void initHistoryRecyclerView() {
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerview.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_history) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, String o) {

            }
        });
    }

    private void initSearchRecyclerView() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.index_item) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, String o) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchStr = s.toString();
                if (!TextUtils.isEmpty(searchStr)) {
                    mvpPresenter.searchBook(searchStr);
                }
            }
        });
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