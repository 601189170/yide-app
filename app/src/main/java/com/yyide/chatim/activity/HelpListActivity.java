package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnUpFetchListener;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.HelpItemAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.model.HelpRsp;
import com.yyide.chatim.presenter.HelpIntroductionPresenter;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.HelpIntroductionView;
import com.yyide.chatim.view.SpacesItemDecoration;
import com.yyide.chatim.view.YDVideo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpListActivity extends BaseMvpActivity<HelpIntroductionPresenter> implements HelpIntroductionView {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private HelpItemAdapter adapter;
    private int pageNum = 1;
    private int pageSize = 20;

    @Override
    public int getContentViewID() {
        return R.layout.activity_help_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    @Override
    protected HelpIntroductionPresenter createPresenter() {
        return new HelpIntroductionPresenter(this);
    }

    private void initAdapter() {
        String type = getIntent().getStringExtra("helpType");
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HelpItemAdapter(null);
        recyclerview.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty);
        adapter.getUpFetchModule().setUpFetchEnable(true);
        adapter.getUpFetchModule().setUpFetching(true);
        adapter.getUpFetchModule().setOnUpFetchListener(new OnUpFetchListener() {
            @Override
            public void onUpFetch() {
                adapter.getUpFetchModule().setUpFetching(true);
                //pageNum++;
//                if ("helpAdvanced".equals(type)) {
//                    mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
//                } else {
//                    mvpPresenter.getHelpList(pageSize, pageNum);
//                }
            }
        });
        //recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this, 20)));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                HelpItemRep.Records.HelpItemBean itemBean = (HelpItemRep.Records.HelpItemBean) adapter.getData().get(position);
                Intent intent = new Intent(mActivity, HelpInfoActivity.class);
                intent.putExtra("itemBean", itemBean);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            pageNum = 1;
            if ("helpAdvanced".equals(type)) {
                mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
            } else {
                mvpPresenter.getHelpList(pageSize, pageNum);
            }
        });
        if ("helpAdvanced".equals(type)) {
            title.setText("进阶指南");
            mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
        } else {
            title.setText("入门指南");
            mvpPresenter.getHelpList(pageSize, pageNum);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.stop();
        }
    }

    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void getHelpListSuccess(HelpItemRep model) {
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.getUpFetchModule().setUpFetching(false);
        if (pageNum == 1) {
            if (model != null && model.getData() != null) {
                adapter.setList(model.getData().getRecords());
            }
        } else {
            pageNum++;
            if (model != null && model.getData() != null) {
                adapter.addData(model.getData().getRecords());
            }
        }

    }

    @Override
    public void getHelpListFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.getUpFetchModule().setUpFetching(false);
        Log.d("getHelpListFail", "getHelpListFail:" + msg);
    }

    @Override
    public void getHelpAdvancedSuccess(HelpItemRep model) {
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.getUpFetchModule().setUpFetching(false);
        if (pageNum == 1) {
            if (model != null && model.getData() != null) {
                adapter.setList(model.getData().getRecords());
            }
        } else {
            pageNum++;
            if (model != null && model.getData() != null) {
                adapter.addData(model.getData().getRecords());
            }
        }
    }

    @Override
    public void getHelpAdvancedFails(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.getUpFetchModule().setUpFetching(false);
        Log.d("getHelpAdvancedFails", "getHelpAdvancedFails:" + msg);
    }

    @Override
    public void showError() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
