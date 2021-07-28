package com.yyide.chatim.fragment.leave;

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
import com.yyide.chatim.R;
import com.yyide.chatim.activity.leave.LeaveFlowDetailActivity;
import com.yyide.chatim.adapter.leave.AskForLeaveListAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.AskForLeaveRecordRsp;
import com.yyide.chatim.model.LeaveListRsp;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.presenter.leave.AskForLeaveListPresenter;
import com.yyide.chatim.view.leave.AskForLeaveListView;

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

    @BindView(R.id.blank_page)
    LinearLayout blank_page;

    private AskForLeaveListAdapter adapter;

    private List<LeaveListRsp.DataBean.RecordsBean> list;

    private boolean refresh = false;
    private int curIndex = 1;
    private int pages = 1;
    private int size = 10;

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
        adapter = new AskForLeaveListAdapter(getActivity(), list);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(monScrollListener);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemOnClickListener(position -> {
            Log.d(TAG, "position: " + list.get(position));
            final Intent intent = new Intent(getActivity(), LeaveFlowDetailActivity.class);
            intent.putExtra("id", (long) list.get(position).getId());
            startActivityForResult(intent,REQUEST_CODE);
        });

        refresh = true;
        swipeRefreshLayout.setRefreshing(true);
        //initData();
        mvpPresenter.getAskLeaveRecord(curIndex, size);
    }

//    private void initData() {
//        for (int i = 0; i < 10; i++) {
//            int status = (int) (Math.random() * 4) + 1;
//            Log.d(TAG, "initData: " + status);
//            //i,status,"某某某提交的请假"+i,"2021.03.2"+i
//            final AskForLeaveRecordRsp askForLeaveRecordRsp = new AskForLeaveRecordRsp();
//            askForLeaveRecordRsp.setId(i);
//            askForLeaveRecordRsp.setTitle("某某某提交的请假" + i);
//            askForLeaveRecordRsp.setDate("2021.03.2" + i);
//            askForLeaveRecordRsp.setStatus(status);
//            list.add(askForLeaveRecordRsp);
//        }
//        refresh = false;
//        swipeRefreshLayout.setRefreshing(false);
//        adapter.notifyDataSetChanged();
//    }

    public void showBlankPage() {
        if (list.isEmpty()) {
            blank_page.setVisibility(View.VISIBLE);
        } else {
            blank_page.setVisibility(View.GONE);
        }
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
        mvpPresenter.getAskLeaveRecord(curIndex, size);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: "+requestCode+", "+resultCode);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
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
        if (leaveListRsp.getData() == null){
            ToastUtils.showShort(leaveListRsp.getMsg());
            showBlankPage();
            return;
        }

        final List<LeaveListRsp.DataBean.RecordsBean> records = leaveListRsp.getData().getRecords();
        pages = leaveListRsp.getData().getPages();
        adapter.setIsLastPage(curIndex == pages);
        adapter.setOnlyOnePage(pages <= 1);
        adapter.setIsLoadMore(!records.isEmpty());

        list.addAll(records);
        adapter.notifyDataSetChanged();
        showBlankPage();
    }

    @Override
    public void leaveListFail(String msg) {
        Log.e(TAG, "leaveListFail: " + msg);
        refresh = false;
        swipeRefreshLayout.setRefreshing(false);
        showBlankPage();
        adapter.setIsLoadMore(false);
        adapter.notifyDataSetChanged();
    }

    private int mLastVisibleItemPosition;
    private RecyclerView.OnScrollListener monScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (adapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    if (curIndex >= pages) {
                        //ToastUtils.showShort("没有更多数据了！");
                        return;
                    }
                    //发送网络请求获取更多数据
                    mvpPresenter.getAskLeaveRecord(++curIndex, size);
                }
            }
        }
    };
}