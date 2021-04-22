package com.yyide.chatim.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.UserNoticeListAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.AgentInformationRsp;
import com.yyide.chatim.model.HelpRsp;
import com.yyide.chatim.model.UserNoticeRsp;
import com.yyide.chatim.presenter.UserNoticePresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;
import com.yyide.chatim.view.UserNoticeView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageNoticeActivity extends BaseMvpActivity<UserNoticePresenter> implements UserNoticeView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MessageNoticeActivity";
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.blank_page)
    LinearLayout blank_page;
    private boolean refresh = false;
    //private BaseQuickAdapter adapter;
    private List<UserNoticeRsp.DataBean.RecordsBean> list = new ArrayList<>();
    private UserNoticeListAdapter userNoticeListAdapter;
    private int curIndex = 1;
    private int pages = 1;

    @Override
    public int getContentViewID() {
        return R.layout.activity_message_notice_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("消息通知");
        initAdapter();
        swipeRefreshLayout.setOnRefreshListener(this);
        refresh = true;
        swipeRefreshLayout.setRefreshing(true);
        mvpPresenter.getUserNoticePage(1,curIndex,5);
    }

    @Override
    protected UserNoticePresenter createPresenter() {
        return new UserNoticePresenter(this);
    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        userNoticeListAdapter = new UserNoticeListAdapter(this, list);
        recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this, 20)));
        recyclerview.setAdapter(userNoticeListAdapter);
        recyclerview.addOnScrollListener(mOnScrollListener);
        userNoticeListAdapter.setOnItemOnClickListener(position ->{
            Log.e(TAG, "initAdapter: "+position );
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back_layout, R.id.title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
            case R.id.title:
                finish();
                break;
        }
    }

    @Override
    public void getUserNoticePageSuccess(UserNoticeRsp userNoticeRsp) {
        if (userNoticeRsp.getCode() == 200) {
            UserNoticeRsp.DataBean data = userNoticeRsp.getData();
            pages = data.getPages();
            if (refresh) {
                list.clear();
                refresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            List<UserNoticeRsp.DataBean.RecordsBean> records = data.getRecords();
            userNoticeListAdapter.setIsLastPage(curIndex == pages);
            userNoticeListAdapter.setIsLoadMore(!records.isEmpty());
            list.addAll(records);
            userNoticeListAdapter.notifyDataSetChanged();
            showBlankPage();
        }
    }

    @Override
    public void getUserNoticePageFail(String msg) {
        ToastUtils.showShort(msg);
        refresh = false;
        swipeRefreshLayout.setRefreshing(false);
        showBlankPage();
        userNoticeListAdapter.setIsLoadMore(false);
        userNoticeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    @Override
    public void onRefresh() {
        curIndex = 1;
        refresh = true;
        mvpPresenter.getUserNoticePage(1,1,5);
    }

    public void showBlankPage() {
        if (list.isEmpty()) {
            blank_page.setVisibility(View.VISIBLE);
        } else {
            blank_page.setVisibility(View.GONE);
        }
    }

    private int mLastVisibleItemPosition;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (userNoticeListAdapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == userNoticeListAdapter.getItemCount()) {
                    if (curIndex>=pages){
                        //ToastUtils.showShort("没有更多数据了！");
                        return;
                    }
                    //发送网络请求获取更多数据
                    mvpPresenter.getUserNoticePage(1, ++curIndex, 5);
                }
            }
        }
    };
}
