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

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.presenter.AppMannagerPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.AppManagerView;

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

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fl_app_sort)
    FrameLayout fl_app_sort;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private MyAppListRsp myAppListRsp = new MyAppListRsp();
    private BaseQuickAdapter<MyAppListRsp.DataBean, BaseViewHolder> baseQuickAdapter;
    private MyAppListRsp.DataBean dataBean;

    @Override
    public int getContentViewID() {
        return R.layout.activity_app_manager_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        title.setText(R.string.app_commonly_title);
        initAdapter();
        mvpPresenter.getMyAppList();
    }

    @Override
    protected AppMannagerPresenter createPresenter() {
        return new AppMannagerPresenter(this);
    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        baseQuickAdapter = new BaseQuickAdapter<MyAppListRsp.DataBean, BaseViewHolder>(R.layout.item_manager) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, MyAppListRsp.DataBean item) {
                ImageView iv_app_icon = holder.getView(R.id.iv_app_icon);
                ImageView iv_del = holder.getView(R.id.iv_del);
                TextView name = holder.getView(R.id.tv_app_name);
                name.setText(item.getName());
                if ("editor".equals(item.getAppType())) {
                    iv_del.setVisibility(View.GONE);
                    iv_app_icon.setImageResource(R.drawable.icon_bj);
                } else {
                    iv_del.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(item.getImg())) {
                        GlideUtil.loadCircleImage(AppManagerActivity.this, item.getImg(), iv_app_icon);
                    }
                }
                iv_del.setOnClickListener(v -> {
                    dataBean = item;
                    mvpPresenter.deleteApp(item.getId());
                });
            }
        };

        recyclerview.setAdapter(baseQuickAdapter);
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyAppListRsp.DataBean item = (MyAppListRsp.DataBean) adapter.getItem(position);
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

    @OnClick({R.id.back_layout, R.id.fl_app_sort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.fl_app_sort:
                Intent intent = new Intent(this, AppSortActivity.class);
                intent.putExtra("appBean", myAppListRsp);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void getMyAppListSuccess(MyAppListRsp model) {
        hideLoading();
        if (model != null && model.getData() != null) {
            List<MyAppListRsp.DataBean> data = model.getData();
            fl_app_sort.setVisibility(View.VISIBLE);
            myAppListRsp = model;
            MyAppListRsp.DataBean itemBean = new MyAppListRsp.DataBean();
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
