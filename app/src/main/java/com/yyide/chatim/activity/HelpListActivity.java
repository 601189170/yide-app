package com.yyide.chatim.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.HelpItemAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.presenter.HelpIntroductionPresenter;
import com.yyide.chatim.view.HelpIntroductionView;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpListActivity extends BaseMvpActivity<HelpIntroductionPresenter> implements HelpIntroductionView,
        SwipeRefreshLayout.OnRefreshListener {
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
    private String type;

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

    @Override
    public void onRefresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        adapter.getLoadMoreModule().setEnableLoadMore(false);
        pageNum = 1;
        if ("helpAdvanced".equals(type)) {
            mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
        } else {
            mvpPresenter.getHelpList(pageSize, pageNum);
        }
    }

    private void initAdapter() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        type = getIntent().getStringExtra("helpType");
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HelpItemAdapter(null);
        recyclerview.setAdapter(adapter);
//        adapter.addFooterView();
        adapter.setEmptyView(R.layout.empty);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            HelpItemRep.Records.HelpItemBean itemBean = (HelpItemRep.Records.HelpItemBean) adapter.getData().get(position);
            if (itemBean.getItemType() == 1) {
                Intent intent = new Intent(mActivity, HelpInfoActivity.class);
                intent.putExtra("itemBean", itemBean);
                startActivity(intent);
            }
        });

        if ("helpAdvanced".equals(type)) {
            title.setText("教学视频");
            mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
        } else {
            title.setText("功能指南");
            mvpPresenter.getHelpList(pageSize, pageNum);
        }
        initLoadMore();
    }

    private void initLoadMore() {
        adapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            //上拉加载时取消下拉刷新
            mSwipeRefreshLayout.setRefreshing(false);
            adapter.getLoadMoreModule().setEnableLoadMore(true);
            //请求数据
            pageNum++;
            if ("helpAdvanced".equals(type)) {
                mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
            } else {
                mvpPresenter.getHelpList(pageSize, pageNum);
            }
        });
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.stop();
        }
        GSYVideoManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void getHelpListSuccess(HelpItemRep model) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (pageNum == 1) {
                if (model != null && model.getData() != null) {
                    adapter.setList(model.getData().getRecords());
                }
            } else {
                if (model != null && model.getData() != null) {
                    adapter.addData(model.getData().getRecords());
                }
            }
            if (model.getData() != null && model.getData().getRecords() != null) {
                if (model.getData().getRecords().size() < pageSize) {
                    //如果不够一页,显示没有更多数据布局
                    adapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    adapter.getLoadMoreModule().loadMoreComplete();
                }
            }
        }
    }

    @Override
    public void getHelpListFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d("getHelpListFail", "getHelpListFail:" + msg);
    }

    @Override
    public void showError() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
