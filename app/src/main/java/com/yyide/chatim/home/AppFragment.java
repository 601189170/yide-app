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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.AppManagerActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.activity.notice.NoticeAnnouncementActivity;
import com.yyide.chatim.adapter.AppAdapter;
import com.yyide.chatim.adapter.MyAppItemAdapter;
import com.yyide.chatim.adapter.RecylAppAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.presenter.AppPresenter;
import com.yyide.chatim.view.AppView;
import com.yyide.chatim.view.YDNestedScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


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
//    @BindView(R.id.nestedScrollView)
//    YDNestedScrollView nestedScrollView;
    @BindView(R.id.recy2)
    RecyclerView mStickView;
    private View mBaseView;
    RecylAppAdapter recylAppAdapter;
    AppAdapter appAdapter;
    boolean sc = true;
    MyAppItemAdapter adapter;
    private String TAG = AppFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.app_layout, container, false);
        return mBaseView;
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        adapter = new MyAppItemAdapter();
        //我的应用
        mygrid.setAdapter(adapter);
        mygrid.setOnItemClickListener((parent, view1, position, id) -> {
            if ("editor".equals(adapter.getItem(position).getAppType())) {
                Intent intent = new Intent(mActivity, AppManagerActivity.class);
                startActivity(intent);
            } else {
                if ("通知公告".equals(adapter.getItem(position).getName())) {
                    Intent intent = new Intent(getContext(), NoticeAnnouncementActivity.class);
                    startActivity(intent);
                } else {
                    if ("#".equals(adapter.getItem(position).getPath().trim())) {
                        ToastUtils.showShort("暂无权限");
                    } else {
                        Intent intent = new Intent(getContext(), WebViewActivity.class);
                        intent.putExtra("url", adapter.getItem(position).getPath());
                        startActivity(intent);
                    }
                }
            }
        });
//        处理吸顶菜单是否显示
//        nestedScrollView.setNeedScroll(false);
//        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if (scrollY > (ll_my_app.getHeight() + SizeUtils.dp2px(20))) {
//                mStickView.setVisibility(View.VISIBLE);
//            } else {
//                mStickView.setVisibility(View.GONE);
//            }
//        });

        //菜单列表
        recylAppAdapter = new RecylAppAdapter();
        recy.setAdapter(recylAppAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy.setLayoutManager(linearLayoutManager);
        recylAppAdapter.setOnItemClickListener((view12, position) -> {
            sc = false;
            recylAppAdapter.setPosition(position);
//            recyclerViewApp.smoothScrollToPosition(position);
            recy.smoothScrollToPosition(position);
            mStickView.smoothScrollToPosition(position);
            ((LinearLayoutManager)recyclerViewApp.getLayoutManager()).scrollToPositionWithOffset(position,0);
        });
//        recy.setNestedScrollingEnabled(false);
//        listview.setNestedScrollingEnabled(false);
        mStickView.setAdapter(recylAppAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mActivity);
        linearLayoutManager2.setOrientation(linearLayoutManager2.HORIZONTAL);
        mStickView.setLayoutManager(linearLayoutManager2);

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
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//停止滑动
                    if (recyclerView.canScrollVertically(1)) {
                        mSwipeRefreshLayout.setEnabled(true);
                    } else {
                        mSwipeRefreshLayout.setEnabled(false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePosition = l.findFirstVisibleItemPosition();
                if (sc) {
                    recylAppAdapter.setPosition(firstVisiblePosition);
                    recy.smoothScrollToPosition(firstVisiblePosition);
                    mStickView.smoothScrollToPosition(firstVisiblePosition);
                }
            }
        });

        rightBtn.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, AppManagerActivity.class));
        });
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
                appAdapter.setList(model.getData().getRecords());
            }
        }
    }

    @Override
    public void getAppListFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getAppListFail :" + msg);
    }

}
