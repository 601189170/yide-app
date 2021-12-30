package com.yyide.chatim.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.AppManagerActivity;
import com.yyide.chatim.adapter.AppAdapter;
import com.yyide.chatim.adapter.MyAppItemAdapter;
import com.yyide.chatim.adapter.RecylAppAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.presenter.AppPresenter;
import com.yyide.chatim.utils.JumpUtil;
import com.yyide.chatim.view.AppView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;


public class AppFragment extends BaseMvpFragment<AppPresenter> implements AppView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.myRecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.listview)
    RecyclerView recyclerViewApp;
    @BindView(R.id.right_btn)
    LinearLayout rightBtn;
    @BindView(R.id.ll_my_app)
    LinearLayout ll_my_app;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    private View mBaseView;
    RecylAppAdapter recylAppAdapter;
    AppAdapter appAdapter;
    boolean sc = true;
    MyAppItemAdapter adapter;
    private String TAG = AppFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.app_fragment_new, container, false);
        return mBaseView;
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) ->
                mSwipeRefreshLayout.setEnabled(verticalOffset >= 0)
        );
        adapter = new MyAppItemAdapter();
        //我的应用
        myRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
        myRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view1, position) -> {
            final MyAppListRsp.DataBean item = (MyAppListRsp.DataBean) adapter.getItem(position);
            JumpUtil.appOpen(requireContext(), item.getName(), item.getPath());
        });

        //菜单列表
        recylAppAdapter = new RecylAppAdapter();
        recy.setAdapter(recylAppAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy.setLayoutManager(linearLayoutManager);
        recylAppAdapter.setOnItemClickListener((view12, position) -> {
            appBarLayout.setExpanded(false);
            sc = false;
            recylAppAdapter.setPosition(position);
//            recyclerViewApp.smoothScrollToPosition(position);
            recy.smoothScrollToPosition(position);
            ((LinearLayoutManager) Objects.requireNonNull(recyclerViewApp.getLayoutManager())).scrollToPositionWithOffset(position, 0);
        });

//        recy.setNestedScrollingEnabled(false);
//        recyclerViewApp.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mActivity);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        //其他应用列表
        appAdapter = new AppAdapter(R.layout.app_item);
        recyclerViewApp.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerViewApp.setAdapter(appAdapter);
        recyclerViewApp.setOnTouchListener((v, event) -> {
            sc = true;
            return false;
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        onRefresh();
        recyclerViewApp.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert l != null;
                int firstVisiblePosition = l.findFirstVisibleItemPosition();
                if (sc) {
                    recylAppAdapter.setPosition(firstVisiblePosition);
                    recy.smoothScrollToPosition(firstVisiblePosition);
                }
            }
        });

        rightBtn.setOnClickListener(v -> startActivity(new Intent(mActivity, AppManagerActivity.class)));
    }

    @Override
    public void onRefresh() {
        mvpPresenter.getMyAppList();
        mvpPresenter.getAppList();
        mvpPresenter.queryUserBarrierPermissions();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_APP_MANAGER.equals(messageEvent.getCode())) {
            mvpPresenter.getMyAppList();
        } else if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            mvpPresenter.getMyAppList();
            mvpPresenter.getAppList();
            mvpPresenter.queryUserBarrierPermissions();
        }
    }

    @Override
    protected AppPresenter createPresenter() {
        return new AppPresenter(this);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void getMyAppListSuccess(MyAppListRsp model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (model != null && model.getData() != null) {
                List<MyAppListRsp.DataBean> data = model.getData();
                MyAppListRsp.DataBean itemBean = new MyAppListRsp.DataBean();
                itemBean.setAppType("editor");
                itemBean.setName("编辑");
                if (data == null) {
                    data = new ArrayList<>();
                }
                data.add(itemBean);
                adapter.setList(data);
            }
        }
    }

    @Override
    public void getMyAppFail(String msg) {
        Log.d(TAG, "getMyAppFail :" + msg);
    }

    @Override
    public void getAppListSuccess(AppItemBean model) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (model.getData() != null && model.getData().getRecords() != null) {
                recylAppAdapter.notifydata(model.getData().getRecords());
                List<AppItemBean.DataBean.RecordsBean> records = model.getData().getRecords();
                appAdapter.setList(records);
            }
        }
    }

    @Override
    public void getAppListFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getAppListFail :" + msg);
    }

}
