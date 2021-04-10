package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.presenter.AppMannagerPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.AppView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

public class AppManagerActivity extends BaseMvpActivity<AppMannagerPresenter> implements AppView {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fz)
    FrameLayout fz;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<AppListRsp.DataBean, BaseViewHolder> baseQuickAdapter;

    @Override
    public int getContentViewID() {
        return R.layout.activity_app_manager_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("应用管理");
        initAdapter();
        mvpPresenter.getMyAppList();
    }

    @Override
    protected AppMannagerPresenter createPresenter() {
        return new AppMannagerPresenter(this);
    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        baseQuickAdapter = new BaseQuickAdapter<AppListRsp.DataBean, BaseViewHolder>(R.layout.item_manager) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, AppListRsp.DataBean item) {
                ImageView iv_app_icon = holder.getView(R.id.iv_app_icon);
                TextView name = holder.getView(R.id.tv_name);
                if (!TextUtils.isEmpty(item.getImg())) {
                    GlideUtil.loadCircleImage(AppManagerActivity.this, item.getImg(), iv_app_icon);
                }
                name.setText(item.getName());
                if ("editor".equals(item.getAppType())) {
                    iv_app_icon.setBackground(getResources().getDrawable(R.drawable.icon_bj));
                }
            }
        };

        recyclerview.setAdapter(baseQuickAdapter);
        baseQuickAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                AppListRsp.DataBean item = (AppListRsp.DataBean) adapter.getItem(position);
                if ("editor".equals(item.getAppType())) {
                    startActivity(new Intent(AppManagerActivity.this, AppAddActivity.class));
                }
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

    @OnClick({R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }

    @Override
    public void getMyAppListSuccess(AppListRsp model) {
        hideLoading();
        if (model != null && model.getData() != null) {
            List<AppListRsp.DataBean> data = model.getData();
            AppListRsp.DataBean itemBean = new AppListRsp.DataBean();
            itemBean.setAppType("editor");
            itemBean.setName("添加");
            if (data == null) {
                data = new ArrayList<>();
            }
            data.add(itemBean);
            baseQuickAdapter.setList(data);
        }
    }

    @Override
    public void getMyAppFail(String msg) {
    }

    @Override
    public void getAppListSuccess(AppItemBean model) {

    }

    @Override
    public void getAppListFail(String msg) {

    }

    @Override
    public void showError() {

    }
}
