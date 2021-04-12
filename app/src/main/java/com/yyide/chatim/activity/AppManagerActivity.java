package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.MainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.presenter.AppMannagerPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.AppManagerView;
import com.yyide.chatim.view.AppView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

public class AppManagerActivity extends BaseMvpActivity<AppMannagerPresenter> implements AppManagerView {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.fz)
    FrameLayout fz;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<AppListRsp.DataBean, BaseViewHolder> baseQuickAdapter;
    private AppListRsp.DataBean dataBean;

    @Override
    public int getContentViewID() {
        return R.layout.activity_app_manager_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
                ImageView iv_del = holder.getView(R.id.iv_del);
                TextView name = holder.getView(R.id.tv_name);
                if (!TextUtils.isEmpty(item.getImg())) {
                    GlideUtil.loadCircleImage(AppManagerActivity.this, item.getImg(), iv_app_icon);
                }
                name.setText(item.getName());
                if ("editor".equals(item.getAppType())) {
                    iv_del.setVisibility(View.GONE);
                    iv_app_icon.setBackground(getResources().getDrawable(R.drawable.icon_bj));
                }
                iv_del.setOnClickListener(v -> {
                    dataBean = item;
                    mvpPresenter.deleteApp(item.getId());
                });
            }
        };

        recyclerview.setAdapter(baseQuickAdapter);
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppListRsp.DataBean item = (AppListRsp.DataBean) adapter.getItem(position);
            if ("editor".equals(item.getAppType())) {
                startActivity(new Intent(AppManagerActivity.this, AppAddActivity.class));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_APP_MANAGER.equals(messageEvent.getCode())) {
            mvpPresenter.getMyAppList();
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
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
    public void deleteSuccess(ResultBean model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            ToastUtils.showShort(model.getMsg());
            if (dataBean != null) {
                baseQuickAdapter.remove(dataBean);
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_APP_MANAGER, ""));
            }
        }
    }

    @Override
    public void deleteFail(String msg) {
        Log.d("deleteFail", "deleteFail: " + msg);
    }

    @Override
    public void showError() {

    }
}
