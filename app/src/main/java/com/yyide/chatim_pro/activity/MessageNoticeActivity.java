package com.yyide.chatim_pro.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.message.NoticeContentActivity;
import com.yyide.chatim_pro.adapter.UserNoticeListAdapter;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BaseMvpActivity;
import com.yyide.chatim_pro.model.EventMessage;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.model.UserMsgNoticeRsp;
import com.yyide.chatim_pro.presenter.UserNoticePresenter;
import com.yyide.chatim_pro.utils.StatusBarUtils;
import com.yyide.chatim_pro.view.SpacesItemDecoration;
import com.yyide.chatim_pro.view.UserNoticeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息通知列表
 */
public class MessageNoticeActivity extends BaseMvpActivity<UserNoticePresenter> implements UserNoticeView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MessageNoticeActivity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    //private BaseQuickAdapter adapter;
    private List<UserMsgNoticeRsp.DataBean.DataItemBean> list = new ArrayList<>();
    private UserNoticeListAdapter userNoticeListAdapter;
    private int curIndex = 1;
    private int pages = 1;
    private int pageSize = 10;

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
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setRefreshing(true);
        mvpPresenter.getUserNoticePage(curIndex, pageSize);
    }

    @Override
    protected UserNoticePresenter createPresenter() {
        return new UserNoticePresenter(this);
    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        userNoticeListAdapter = new UserNoticeListAdapter();
        recyclerview.setAdapter(userNoticeListAdapter);
        recyclerview.addOnScrollListener(mOnScrollListener);
        userNoticeListAdapter.setEmptyView(R.layout.empty);
        userNoticeListAdapter.setOnItemClickListener((adapter, view, position) -> {
            final UserMsgNoticeRsp.DataBean.DataItemBean recordsBean = userNoticeListAdapter.getItem(position);
            Log.e(TAG, "initAdapter: " + recordsBean.toString());
            //模块类型模块类型 1闸机推送 2考勤 3考勤周报 4请假 5通知公告 6失物招领 7作业发布
            switch (recordsBean.getModuleType()) {
                case 5:
                    startActivity(new Intent(MessageNoticeActivity.this, NoticeContentActivity.class));
                    break;
            }
//            if ("2".equals(recordsBean.getIsText())) {
//                //不是纯文本，需要跳转详情
//                switch (recordsBean.getAttributeType()) {
//                    case "1":
//                        //请假
//                        final long callId = recordsBean.getCallId();
//                        final Intent intent = new Intent(this, LeaveFlowDetailActivity.class);
//                        intent.putExtra("id", callId);
//                        startActivity(intent);
//                        break;
//                    case "2":
//                        //调课
//                        break;
//                    default:
//                        break;
//                }
//            } else if ("1".equals(recordsBean.getIsText())) {
//                if ("3".equals(recordsBean.getAttributeType())) {
//                    //跳转人脸采集
//                    startActivity(new Intent(this, FaceCaptureActivity.class));
//                } else {
//                    //其他消息显示文本详情
//                    MessageDetailActivity.start(this, recordsBean);
//                }
//            } else if ("2".equals(recordsBean.getJumpTarget())) {
//                //startActivity(new Intent(this, WeeklyHomeActivity.class));
//            }
        });
        initLoadMore();
    }

    private void initLoadMore() {
        userNoticeListAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            //上拉加载时取消下拉刷新
            swipeRefreshLayout.setRefreshing(false);
            userNoticeListAdapter.getLoadMoreModule().setEnableLoadMore(true);
            //请求数据
            curIndex++;
            mvpPresenter.getUserNoticePage(curIndex, pageSize);
        });
        userNoticeListAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        userNoticeListAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
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
    public void getUserNoticePageSuccess(UserMsgNoticeRsp userNoticeRsp) {
        swipeRefreshLayout.setRefreshing(false);
        if (userNoticeRsp.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            UserMsgNoticeRsp.DataBean data = userNoticeRsp.getData();
            pages = (data.getTotal() / pageSize) + 1;
            if (curIndex == 1) {
                if (data.getList() != null) {
                    userNoticeListAdapter.setList(data.getList());
                }
            } else {
                if (data.getList() != null) {
                    userNoticeListAdapter.addData(data.getList());
                }
            }
            if (data.getList() != null) {
                if (data.getList().size() < pageSize) {
                    //如果不够一页,显示没有更多数据布局
                    userNoticeListAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    userNoticeListAdapter.getLoadMoreModule().loadMoreComplete();
                }
            }
        }
    }

    @Override
    public void updateNoticeSuccess(ResultBean resultBean) {
        if (resultBean.getCode() == BaseConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MESSAGE_UPDATE, ""));
        }
    }

    @Override
    public void messageNumberSuccess(int number, String date, String msg) {

    }

    @Override
    public void messageNumberFail(String msg) {

    }

    @Override
    public void getUserNoticePageFail(String msg) {
        ToastUtils.showShort(msg);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onRefresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        userNoticeListAdapter.getLoadMoreModule().setEnableLoadMore(false);
        curIndex = 1;
        mvpPresenter.getUserNoticePage(1, 5);
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
                    if (curIndex >= pages) {
                        //ToastUtils.showShort("没有更多数据了！");
                        return;
                    }
                    //发送网络请求获取更多数据
                    mvpPresenter.getUserNoticePage(++curIndex, 5);
                }
            }
        }
    };
}
