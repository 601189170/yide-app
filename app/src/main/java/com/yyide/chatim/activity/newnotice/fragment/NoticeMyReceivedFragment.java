package com.yyide.chatim.activity.newnotice.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentNoticeMyReceviedListBinding;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.presenter.NoticeReceivedPresenter;
import com.yyide.chatim.view.NoticeReceivedView;
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知公告
 * auther lrz
 * time  2021年6月17日11:00:27
 */
public class NoticeMyReceivedFragment extends BaseMvpFragment<NoticeReceivedPresenter> implements NoticeReceivedView, View.OnClickListener {

    private final String TAG = NoticeMyReceivedFragment.class.getSimpleName();
    private FragmentNoticeMyReceviedListBinding viewBinding;
    private int pageNum = 1;

    public static NoticeMyReceivedFragment newInstance() {
        NoticeMyReceivedFragment fragment = new NoticeMyReceivedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentNoticeMyReceviedListBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected NoticeReceivedPresenter createPresenter() {
        return new NoticeReceivedPresenter(this);
    }

    private void initView() {
        //默认选中第一个
        viewBinding.tvToday.setChecked(true);
        viewBinding.tvToday.setTextColor(Color.WHITE);
        viewBinding.tvToday.setOnClickListener(this);
        viewBinding.tvThisWeek.setOnClickListener(this);
        viewBinding.tvThisMonth.setOnClickListener(this);
        NoticeMyReceivedAdapter receivedAdapter = new NoticeMyReceivedAdapter(R.layout.item_notice_my_recevied);
        viewBinding.list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        viewBinding.list.addItemDecoration(new ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(12)));
        viewBinding.list.setAdapter(receivedAdapter);
        receivedAdapter.setEmptyView(R.layout.empty);
        receivedAdapter.getEmptyLayout().setOnClickListener(v -> {
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data");
        });
        receivedAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        receivedAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            receivedAdapter.getLoadMoreModule().setEnableLoadMore(true);
            //请求数据
            pageNum++;
            //mvpPresenter.getHelpList(pageSize, pageNum);
        });
        receivedAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        receivedAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        receivedAdapter.setList(list);
    }

    @Override
    public void onClick(View v) {
        viewBinding.tvToday.setChecked(false);
        viewBinding.tvThisWeek.setChecked(false);
        viewBinding.tvThisMonth.setChecked(false);
        viewBinding.tvToday.setTextColor(getContext().getResources().getColor(R.color.text_1E1E1E));
        viewBinding.tvThisWeek.setTextColor(getContext().getResources().getColor(R.color.text_1E1E1E));
        viewBinding.tvThisMonth.setTextColor(getContext().getResources().getColor(R.color.text_1E1E1E));
        if (v.getId() == viewBinding.tvToday.getId()) {
            viewBinding.tvToday.setChecked(true);
            viewBinding.tvToday.setTextColor(Color.WHITE);
        } else if (v.getId() == viewBinding.tvThisWeek.getId()) {
            viewBinding.tvThisWeek.setChecked(true);
            viewBinding.tvThisWeek.setTextColor(Color.WHITE);
        } else if (v.getId() == viewBinding.tvThisMonth.getId()) {
            viewBinding.tvThisMonth.setChecked(true);
            viewBinding.tvThisMonth.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void getMyReceivedList(ResultBean model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
//            if (pageNum == 1) {
//                if (model != null && model.getData() != null) {
//                    adapter.setList(model.getData().getRecords());
//                }
//            } else {
//                if (model != null && model.getData() != null) {
//                    adapter.addData(model.getData().getRecords());
//                }
//            }
//            if (model.getData() != null && model.getData().getRecords() != null) {
//                if (model.getData().getRecords().size() < pageSize) {
//                    //如果不够一页,显示没有更多数据布局
//                    adapter.getLoadMoreModule().loadMoreEnd();
//                } else {
//                    adapter.getLoadMoreModule().loadMoreComplete();
//                }
//            }
        }
    }

    @Override
    public void getMyReceivedFail(String msg) {
        Log.d(TAG, "getMyReceivedFail>>>>>：" + msg);
    }
}