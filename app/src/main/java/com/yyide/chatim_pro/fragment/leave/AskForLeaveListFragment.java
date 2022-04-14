package com.yyide.chatim_pro.fragment.leave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.leave.LeaveFlowDetailActivity;
import com.yyide.chatim_pro.adapter.leave.AskForLeaveListAdapter;
import com.yyide.chatim_pro.base.BaseMvpFragment;
import com.yyide.chatim_pro.model.LeaveListRsp;
import com.yyide.chatim_pro.presenter.leave.AskForLeaveListPresenter;
import com.yyide.chatim_pro.view.leave.AskForLeaveListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AskForLeaveListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskForLeaveListFragment extends BaseMvpFragment<AskForLeaveListPresenter> implements AskForLeaveListView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = AskForLeaveListFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private final int REQUEST_CODE = 1;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private AskForLeaveListAdapter adapter;

    private List<LeaveListRsp.DataBean.RecordsBean> list;

    private boolean refresh = false;
    private int curIndex = 1;
    private int pages = 1;
    private int size = 15;

    public AskForLeaveListFragment() {
        // Required empty public constructor
    }

    public static AskForLeaveListFragment newInstance(String param1) {
        AskForLeaveListFragment fragment = new AskForLeaveListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.e(TAG, "onCreate: " + mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AskForLeaveListAdapter(list);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        mRecyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Log.d(TAG, "position: " + list.get(position));
                final Intent intent = new Intent(getActivity(), LeaveFlowDetailActivity.class);
                intent.putExtra("id", list.get(position).getId() + "");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        refresh = true;
        swipeRefreshLayout.setRefreshing(true);
        //initData();
        mvpPresenter.getAskLeaveRecord(curIndex, size);
        initLoadMore();
    }

    private void initLoadMore() {
        adapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            //上拉加载时取消下拉刷新
            swipeRefreshLayout.setRefreshing(false);
            adapter.getLoadMoreModule().setEnableLoadMore(true);
            //请求数据
            curIndex++;
            mvpPresenter.getAskLeaveRecord(curIndex, size);
        });
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    @Override
    protected AskForLeaveListPresenter createPresenter() {
        return new AskForLeaveListPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onRefresh() {
        curIndex = 1;
        refresh = true;
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        adapter.getLoadMoreModule().setEnableLoadMore(false);
        mvpPresenter.getAskLeaveRecord(curIndex, size);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: " + requestCode + ", " + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            refresh = true;
            curIndex = 1;
            swipeRefreshLayout.setRefreshing(true);
            mvpPresenter.getAskLeaveRecord(curIndex, size);
        }
    }

    @Override
    public void leaveList(LeaveListRsp leaveListRsp) {
        Log.d(TAG, "leaveList: " + leaveListRsp.toString());
        if (refresh) {
            list.clear();
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
        }
        // data is null
        if (leaveListRsp.getData() == null) {
            ToastUtils.showShort(leaveListRsp.getMessage());
            return;
        }

        final List<LeaveListRsp.DataBean.RecordsBean> records = leaveListRsp.getData().getList();
        if (curIndex == 1) {
            adapter.setList(records);
        } else {
            adapter.addData(records);
        }
        if (records.size() < size) {
            //如果不够一页,显示没有更多数据布局
            adapter.getLoadMoreModule().loadMoreEnd();
        } else {
            adapter.getLoadMoreModule().loadMoreComplete();
        }
    }

    @Override
    public void leaveListFail(String msg) {
        Log.e(TAG, "leaveListFail: " + msg);
        refresh = false;
        swipeRefreshLayout.setRefreshing(false);
    }

}