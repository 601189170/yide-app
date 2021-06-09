package com.yyide.chatim.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.AppManagerActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.activity.leave.AskForLeaveActivity;
import com.yyide.chatim.activity.notice.NoticeAnnouncementActivity;
import com.yyide.chatim.adapter.AppAdapter;
import com.yyide.chatim.adapter.MyAppItemAdapter;
import com.yyide.chatim.adapter.RecylAppAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.presenter.AppPresenter;
import com.yyide.chatim.view.AppView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lombok.val;


public class AppFragment extends BaseMvpFragment<AppPresenter> implements AppView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.mygrid)
    GridView mygrid;
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
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
        });
        adapter = new MyAppItemAdapter();
        //我的应用
        mygrid.setAdapter(adapter);
        mygrid.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent;
            final MyAppListRsp.DataBean item = (MyAppListRsp.DataBean) parent.getItemAtPosition(position);
            switch (item.getName()) {
                case "编辑":
                    intent = new Intent(mActivity, AppManagerActivity.class);
                    startActivity(intent);
                    break;
                case "通知公告":
                    intent = new Intent(getActivity(), NoticeAnnouncementActivity.class);
                    startActivity(intent);
                    break;
                case "请假":
                    //ToastUtils.showShort("请假");
                    intent = new Intent(getActivity(), AskForLeaveActivity.class);
                    startActivity(intent);
                    break;
                case "调课":
                    ToastUtils.showShort("调课");
                    break;
                default:
                    if ("#".equals(item.getPath())) {
                        ToastUtils.showShort("暂无权限");
                    } else {
                        intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", item.getPath());
                        startActivity(intent);
                    }
                    break;
            }
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
            ((LinearLayoutManager) recyclerViewApp.getLayoutManager()).scrollToPositionWithOffset(position, 0);
        });

//        recy.setNestedScrollingEnabled(false);
//        recyclerViewApp.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mActivity);
        linearLayoutManager2.setOrientation(linearLayoutManager2.HORIZONTAL);

        //其他应用列表
        appAdapter = new AppAdapter(R.layout.app_item);
        recyclerViewApp.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerViewApp.setAdapter(appAdapter);
        recyclerViewApp.setOnTouchListener((v, event) -> {
            sc = true;
            return false;
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getColor(R.color.colorPrimary));
        onRefresh();
        recyclerViewApp.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//停止滑动
//                    if (recyclerView.canScrollVertically(1)) {
//                        mSwipeRefreshLayout.setEnabled(true);
//                    } else {
//                        mSwipeRefreshLayout.setEnabled(false);
//                    }
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePosition = l.findFirstVisibleItemPosition();
                if (sc) {
                    recylAppAdapter.setPosition(firstVisiblePosition);
                    recy.smoothScrollToPosition(firstVisiblePosition);
                }
            }
        });

        rightBtn.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, AppManagerActivity.class));
        });

    }

    private void setListViewHeight(RecyclerView listView) {
        int totalHeight = 0;
        int i = 0;
        int len = listView.getAdapter().getItemCount();
        while (i < len) {
            View childView = listView.getLayoutManager().findViewByPosition(i);
            totalHeight += childView.getHeight() + SizeUtils.dp2px(10);
            i++;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ScreenUtils.getScreenHeight();
        listView.setLayoutParams(params);
    }

    @Override
    public void onRefresh() {
        mvpPresenter.getMyAppList();
        mvpPresenter.getAppList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_APP_MANAGER.equals(messageEvent.getCode())) {
            mvpPresenter.getMyAppList();
        } else if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            mvpPresenter.getMyAppList();
            mvpPresenter.getAppList();
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
                adapter.notifyData(data);
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
                recyclerViewApp.getViewTreeObserver().addOnGlobalLayoutListener(() -> setListViewHeight(recyclerViewApp));
            }
        }
    }

    @Override
    public void getAppListFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getAppListFail :" + msg);
    }

}
