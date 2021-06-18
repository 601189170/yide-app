package com.yyide.chatim.activity.newnotice.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.newnotice.NoticePushAdapter;
import com.yyide.chatim.databinding.FragmentNoticePushBinding;
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class NoticePushFragment extends Fragment {

    private FragmentNoticePushBinding pushBinding;
    private int pageNum = 1;
    private int checkPosition = 0;

    public static NoticePushFragment newInstance() {
        NoticePushFragment fragment = new NoticePushFragment();
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
        pushBinding = FragmentNoticePushBinding.inflate(getLayoutInflater());
        return pushBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    List<String> listTabs = new ArrayList<>();

    private void initView() {
        listTabs.add("推荐1");
        listTabs.add("推荐2");
        listTabs.add("推荐3");
        listTabs.add("推荐4");
        listTabs.add("推荐5");
        listTabs.add("推荐6");
        listTabs.add("推荐7");
        listTabs.add("推荐8");
        listTabs.add("推荐9");
        //导航TAB 列表
        pushBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        pushBinding.recyclerview.addItemDecoration(new ItemDecorationPowerful(ItemDecorationPowerful.HORIZONTAL_DIV, Color.TRANSPARENT, SizeUtils.dp2px(5)));
        pushBinding.recyclerview.setAdapter(adapter);
        adapter.setList(listTabs);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            checkPosition = position;
            adapter.notifyDataSetChanged();
            pushBinding.recyclerview.smoothScrollToPosition(position);
        });
        //通知模板数据列表
        NoticePushAdapter mAdapter = new NoticePushAdapter(R.layout.item_notice_push);
        pushBinding.list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        pushBinding.list.addItemDecoration(new ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(12)));
        pushBinding.list.setAdapter(mAdapter);
        mAdapter.setList(listTabs);
        mAdapter.setEmptyView(R.layout.empty);
        mAdapter.getEmptyLayout().setOnClickListener(v -> {
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data");
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.getLoadMoreModule().setEnableLoadMore(true);
            //请求数据
            pageNum++;
            //mvpPresenter.getHelpList(pageSize, pageNum);
        });
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        //数据加载完之后
//        if (pageNum == 1) {
//            if (model != null && model.getData() != null) {
//                mAdapter.setList(model.getData().getRecords());
//            }
//        } else {
//            if (model != null && model.getData() != null) {
//                mAdapter.addData(model.getData().getRecords());
//            }
//        }
//        if (model.getData() != null && model.getData().getRecords() != null) {
//            if (model.getData().getRecords().size() < pageSize) {
//                //如果不够一页,显示没有更多数据布局
//                mAdapter.getLoadMoreModule().loadMoreEnd();
//            } else {
//                mAdapter.getLoadMoreModule().loadMoreComplete();
//            }
//        }

    }

    private BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_notice_textview) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, String item) {
            CheckedTextView checkedTextView = holder.getView(R.id.tv_checked_mark);
            checkedTextView.setText(item);
            if (checkPosition == holder.getAdapterPosition()) {
                checkedTextView.setChecked(true);
                checkedTextView.setTextColor(Color.WHITE);
            } else {
                checkedTextView.setTextColor(getContext().getResources().getColor(R.color.text_1E1E1E));
                checkedTextView.setChecked(false);
            }
        }
    };

}