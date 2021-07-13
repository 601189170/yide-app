package com.yyide.chatim.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeAnnouncementFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeAnnouncementFragmentView;
import com.yyide.chatim.adapter.NoticeAnnouncementListAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeAnnouncementListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeAnnouncementListFragment extends BaseMvpFragment<NoticeAnnouncementFragmentPresenter> implements NoticeAnnouncementFragmentView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NoticeAnnouncementListF";
    List<NoticeAnnouncementModel> list = new ArrayList<>();
    //    BaseQuickAdapter adapter;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.blank_page)
    LinearLayout blank_page;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private boolean refresh = false;
    private boolean loading = false;
    private int curIndex = 1;
    private int pages = 1;
    private NoticeAnnouncementListAdapter noticeAnnouncementListAdapter;

    public NoticeAnnouncementListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NoticeAnnouncementListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticeAnnouncementListFragment newInstance(String param1) {
        NoticeAnnouncementListFragment fragment = new NoticeAnnouncementListFragment();
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noticeAnnouncementListAdapter = new NoticeAnnouncementListAdapter(getActivity(), list);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(monScrollListener);
        mRecyclerView.setAdapter(noticeAnnouncementListAdapter);
        noticeAnnouncementListAdapter.setOnItemOnClickListener(position -> {
            Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
            NoticeAnnouncementModel noticeAnnouncementModel = list.get(position);
            if (mParam1.equals("my_notice")) {
                intent.putExtra("type", 1);
            } else {
                intent.putExtra("type", 2);
            }
            intent.putExtra("id", noticeAnnouncementModel.getId());
            intent.putExtra("status", noticeAnnouncementModel.getStatus());
            if (getActivity() instanceof NoticeAnnouncementActivity) {
                NoticeAnnouncementActivity activity = (NoticeAnnouncementActivity) getActivity();
                if (!activity.other) {
                    activity.other = true;
                }
            }
            startActivity(intent);
        });
        mvpPresenter.noticeList(1, curIndex, 10);
        refresh = true;
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof NoticeAnnouncementActivity){
            NoticeAnnouncementActivity activity = (NoticeAnnouncementActivity) getActivity();
            boolean other = activity.other;
            Log.e(TAG, "onStart1: "+other );
           if (other){
               activity.other = false;
               mvpPresenter.noticeList(1, 1, 10);
               refresh = true;
               swipeRefreshLayout.setRefreshing(true);
           }
        }
    }

    @Override
    protected NoticeAnnouncementFragmentPresenter createPresenter() {
        return new NoticeAnnouncementFragmentPresenter(this);
    }

    @Override
    public void noticeList(NoticeListRsp noticeListRsp) {
        Log.e(TAG, "noticeList: " + noticeListRsp.toString());
        List<NoticeListRsp.DataBean.RecordsBean> records = noticeListRsp.getData().getRecords();
        pages = noticeListRsp.getData().getPages();
        noticeAnnouncementListAdapter.setOnlyOnePage(pages <= 1);
        noticeAnnouncementListAdapter.setIsLastPage(curIndex == pages);
        noticeAnnouncementListAdapter.setIsLoadMore(!records.isEmpty());
        if (refresh) {
            list.clear();
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
        }
        if (!records.isEmpty()) {
            for (NoticeListRsp.DataBean.RecordsBean record : records) {
                //yyyy-MM-dd HH:mm 03.06 09:00
                String productionTime = DateUtils.formatTime(record.getProductionTime(), null, null);
                list.add(new NoticeAnnouncementModel(record.getId(), record.getTitle(), record.getProductionTarget(), record.getContent(), productionTime, record.getStatus()));
            }
            //adapter.setList(list);
        }
        noticeAnnouncementListAdapter.notifyDataSetChanged();
        showBlankPage();
    }

    public void showBlankPage() {
        if (list.isEmpty()) {
            blank_page.setVisibility(View.VISIBLE);
        } else {
            blank_page.setVisibility(View.GONE);
        }
    }

    @Override
    public void noticeListFail(String msg) {
        Log.e(TAG, "noticeListFail: " + msg);
        refresh = false;
        swipeRefreshLayout.setRefreshing(false);
        showBlankPage();
        noticeAnnouncementListAdapter.setIsLoadMore(false);
        noticeAnnouncementListAdapter.notifyDataSetChanged();
    }



    @Override
    public void onRefresh() {
        curIndex = 1;
        refresh = true;
        mvpPresenter.noticeList(1, 1, 10);
    }

    private int mLastVisibleItemPosition;
    private RecyclerView.OnScrollListener monScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (noticeAnnouncementListAdapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == noticeAnnouncementListAdapter.getItemCount()) {
                    loading = true;
                    if (curIndex>=pages){
                        //ToastUtils.showShort("没有更多数据了！");
                        return;
                    }
                    //发送网络请求获取更多数据
                    mvpPresenter.noticeList(1, ++curIndex, 10);
                }
            }
        }
    };
}