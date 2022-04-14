package com.yyide.chatim_pro.activity.notice.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.notice.NoticeAnnouncementActivity;
import com.yyide.chatim_pro.activity.notice.NoticeDetailActivity;
import com.yyide.chatim_pro.activity.notice.presenter.PublishAnnouncementFragmentPresenter;
import com.yyide.chatim_pro.activity.notice.view.PublishAnnouncementFragmentView;
import com.yyide.chatim_pro.adapter.PublishNoticeAnnouncementListAdapter;
import com.yyide.chatim_pro.base.BaseMvpFragment;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.NoticeListRsp;
import com.yyide.chatim_pro.view.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublishNoticAnnouncementListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishNoticAnnouncementListFragment extends BaseMvpFragment<PublishAnnouncementFragmentPresenter> implements PublishAnnouncementFragmentView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NoticeAnnouncementListF";
    List<NoticeListRsp.DataBean.RecordsBean> list = new ArrayList<>();
    //BaseQuickAdapter adapter;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean refresh = false;

    @BindView(R.id.blank_page)
    LinearLayout blank_page;

    private boolean loading = false;
    private int curIndex = 1;
    private int pages = 1;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private PublishNoticeAnnouncementListAdapter publishNoticeAnnouncementListAdapter;

    public PublishNoticAnnouncementListFragment() {
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
    public static PublishNoticAnnouncementListFragment newInstance(String param1) {
        PublishNoticAnnouncementListFragment fragment = new PublishNoticAnnouncementListFragment();
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

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        publishNoticeAnnouncementListAdapter = new PublishNoticeAnnouncementListAdapter(getActivity(), list);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(monScrollListener);
        mRecyclerView.setAdapter(publishNoticeAnnouncementListAdapter);
        publishNoticeAnnouncementListAdapter.setOnItemOnClickListener(new PublishNoticeAnnouncementListAdapter.OnItemOnClickListener() {
            @Override
            public void onClicked(int position) {
                NoticeListRsp.DataBean.RecordsBean record = list.get(position);
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra("type", 3);
                intent.putExtra("id", record.getId());
                intent.putExtra("status", record.getStatus());
                intent.putExtra("sendObject", record.getSendObject());
                startActivity(intent);
            }

            @Override
            public void delete(int position) {
                long id = list.get(position).getId();
                DialogUtil.showAlertDialog(getActivity(), "确定删除？", "删除通知公告并将其他相关信息一并删除", "取消", "确定", new DialogUtil.OnClickListener() {
                    @Override
                    public void onCancel(View view) {

                    }

                    @Override
                    public void onEnsure(View view) {
                        Log.e(TAG, "删除: " + id);
                        mvpPresenter.delAnnouncement(id);
                    }
                });
            }
        });
        mvpPresenter.noticeList(2, curIndex, 10);
        refresh = true;
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        if (getActivity() instanceof NoticeAnnouncementActivity) {
            mvpPresenter.noticeList(2, 1, 10);
            refresh = true;
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        if (publishNoticeAnnouncementListAdapter != null) {
            publishNoticeAnnouncementListAdapter.cancelTimer();
        }
    }

    @Override
    protected PublishAnnouncementFragmentPresenter createPresenter() {
        return new PublishAnnouncementFragmentPresenter(this);
    }

    @Override
    public void noticeList(NoticeListRsp noticeListRsp) {
        Log.e(TAG, "noticeList: " + noticeListRsp.toString());
        pages = noticeListRsp.getData().getPages();
        List<NoticeListRsp.DataBean.RecordsBean> records = noticeListRsp.getData().getRecords();
        publishNoticeAnnouncementListAdapter.setIsLoadMore(!records.isEmpty());
        publishNoticeAnnouncementListAdapter.setIsLastPage(curIndex == pages);
        publishNoticeAnnouncementListAdapter.setOnlyOnePage(pages <= 1);
        if (refresh) {
            list.clear();
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
        }

        if (!records.isEmpty()) {
            list.addAll(records);
        }
        if (publishNoticeAnnouncementListAdapter != null) {
            publishNoticeAnnouncementListAdapter.cancelTimer();
            publishNoticeAnnouncementListAdapter.notifyDataSetChanged();
        }
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
        publishNoticeAnnouncementListAdapter.setIsLoadMore(false);
        publishNoticeAnnouncementListAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteAnnouncement(BaseRsp baseRsp) {
        Log.e(TAG, "deleteAnnouncement: " + baseRsp.toString());
        if (baseRsp.getCode() == 200) {
            ToastUtils.showShort("删除成功！");
            curIndex = 1;
            refresh = true;
            mvpPresenter.noticeList(2, 1, 10);
        }
    }

    @Override
    public void deleteFail(String msg) {
        ToastUtils.showShort("删除失败:" + msg);
    }

    @Override
    public void onRefresh() {
        curIndex = 1;
        refresh = true;
        mvpPresenter.noticeList(2, 1, 10);
    }

    private int mLastVisibleItemPosition;
    private RecyclerView.OnScrollListener monScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (publishNoticeAnnouncementListAdapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == publishNoticeAnnouncementListAdapter.getItemCount()) {
                    loading = true;
                    //发送网络请求获取更多数据
                    if (curIndex >= pages) {
                        //ToastUtils.showShort("没有更多数据了！");
                        return;
                    }
                    mvpPresenter.noticeList(2, ++curIndex, 10);
                }
            }
        }
    };
}