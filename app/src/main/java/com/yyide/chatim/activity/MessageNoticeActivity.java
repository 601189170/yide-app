package com.yyide.chatim.activity;


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
import com.yyide.chatim.R;
import com.yyide.chatim.activity.face.FaceCaptureActivity;
import com.yyide.chatim.activity.leave.LeaveFlowDetailActivity;
import com.yyide.chatim.adapter.UserNoticeListAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.UserMsgNoticeRsp;
import com.yyide.chatim.presenter.UserNoticePresenter;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;
import com.yyide.chatim.view.UserNoticeView;

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

    @BindView(R.id.blank_page)
    LinearLayout blank_page;
    private boolean refresh = false;
    //private BaseQuickAdapter adapter;
    private List<UserMsgNoticeRsp.DataBean.RecordsBean> list = new ArrayList<>();
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
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refresh = true;
        swipeRefreshLayout.setRefreshing(true);
        mvpPresenter.getUserNoticePage(curIndex, 5);
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
        userNoticeListAdapter.setOnItemOnClickListener(position -> {
            final UserMsgNoticeRsp.DataBean.RecordsBean recordsBean = list.get(position);
            Log.e(TAG, "initAdapter: " + recordsBean.toString());
            if ("2".equals(recordsBean.getIsText())) {
                //不是纯文本，需要跳转详情
                switch (recordsBean.getAttributeType()) {
                    case "1":
                        //请假
                        final long callId = recordsBean.getCallId();
                        final Intent intent = new Intent(this, LeaveFlowDetailActivity.class);
                        intent.putExtra("id", callId);
                        startActivity(intent);
                        break;
                    case "2":
                        //调课
                        break;
                    default:
                        break;
                }
            } else if ("1".equals(recordsBean.getIsText())) {
                if ("3".equals(recordsBean.getAttributeType())) {
                    //跳转人脸采集
                    startActivity(new Intent(this, FaceCaptureActivity.class));
                } else {
                    //其他消息显示文本详情
                    MessageDetailActivity.start(this, recordsBean);
                }
            } else if ("2".equals(recordsBean.getJumpTarget())) {
                //startActivity(new Intent(this, WeeklyHomeActivity.class));
            }
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
    public void getUserNoticePageSuccess(UserMsgNoticeRsp userNoticeRsp) {
        if (userNoticeRsp.getCode() == 200) {
            UserMsgNoticeRsp.DataBean data = userNoticeRsp.getData();
            pages = data.getPages();
            if (refresh) {
                list.clear();
                refresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            List<UserMsgNoticeRsp.DataBean.RecordsBean> records = data.getRecords();
            userNoticeListAdapter.setIsLastPage(curIndex == pages);
            userNoticeListAdapter.setOnlyOnePage(pages <= 1);
            userNoticeListAdapter.setIsLoadMore(!records.isEmpty());
            list.addAll(records);
            userNoticeListAdapter.notifyDataSetChanged();
            showBlankPage();
        }else {
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
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
        mvpPresenter.getUserNoticePage(1, 5);
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
