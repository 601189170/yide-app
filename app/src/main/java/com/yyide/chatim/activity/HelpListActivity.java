package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.model.HelpRsp;
import com.yyide.chatim.presenter.HelpIntroductionPresenter;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.HelpIntroductionView;
import com.yyide.chatim.view.SpacesItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpListActivity extends BaseMvpActivity<HelpIntroductionPresenter> implements HelpIntroductionView {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseQuickAdapter adapter;
    private int pageNum = 1;
    private int pageSize = 20;

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

    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<HelpItemRep.Records.HelpItemBean, BaseViewHolder>(R.layout.item_help) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, HelpItemRep.Records.HelpItemBean o) {
                baseViewHolder
                        .setText(R.id.title, o.getName())
                        .setText(R.id.info, o.getMessage());
            }
        };

        recyclerview.setAdapter(adapter);
        //recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this, 20)));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                HelpItemRep.Records.HelpItemBean itemBean = (HelpItemRep.Records.HelpItemBean) adapter.getData().get(position);
                Intent intent = new Intent(mActivity, HelpInfoActivity.class);
                intent.putExtra("itemBean", itemBean);
                startActivity(intent);
            }
        });

        String type = getIntent().getStringExtra("helpType");
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            pageNum = 1;
            if ("helpAdvanced".equals(type)) {
                mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
            } else {
                mvpPresenter.getHelpList(pageSize, pageNum);
            }
        });
        if ("helpAdvanced".equals(type)) {
            title.setText("进阶指南");
            mvpPresenter.getHelpAdvancedList(pageSize, pageNum);
        } else {
            title.setText("入门指南");
            mvpPresenter.getHelpList(pageSize, pageNum);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void getHelpListSuccess(HelpItemRep model) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (pageNum == 1) {
            if (model != null && model.getData() != null) {
                adapter.setList(model.getData().getRecords());
            }
        } else {
            pageNum++;
            if (model != null && model.getData() != null) {
                adapter.addData(model.getData().getRecords());
            }
        }

    }

    @Override
    public void getHelpListFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d("getHelpListFail", "getHelpListFail:" + msg);
    }

    @Override
    public void getHelpAdvancedSuccess(HelpItemRep model) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (pageNum == 1) {
            if (model != null && model.getData() != null) {
                adapter.setList(model.getData().getRecords());
            }
        } else {
            pageNum++;
            if (model != null && model.getData() != null) {
                adapter.addData(model.getData().getRecords());
            }
        }
    }

    @Override
    public void getHelpAdvancedFails(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d("getHelpAdvancedFails", "getHelpAdvancedFails:" + msg);
    }

    @Override
    public void showError() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
