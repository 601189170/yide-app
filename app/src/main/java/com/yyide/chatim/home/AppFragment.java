package com.yyide.chatim.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ScreenUtils;
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
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.presenter.AppPresenter;
import com.yyide.chatim.view.AppView;

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
    ListView listview;
    @BindView(R.id.right_btn)
    LinearLayout rightBtn;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
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
        recylAppAdapter = new RecylAppAdapter();
        recy.setAdapter(recylAppAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy.setLayoutManager(linearLayoutManager);
        recylAppAdapter.setOnItemClickListener((view12, position) -> {
            sc = false;
            recylAppAdapter.setPosition(position);
            /**准确定位到指定位置，并且将指定位置的item置顶，
             若直接调用scrollToPosition(...)方法，则不会置顶。**/
//            linearLayoutManager.scrollToPositionWithOffset(position, 0);
//            linearLayoutManager.setStackFromEnd(true);
//            listview.setSelection(position);
//            listview.setSelectionFromTop(position, 0);
//            listview.smoothScrollToPositionFromTop(position, 0);
            listview.smoothScrollToPosition(position);
        });
        appAdapter = new AppAdapter();
        listview.setAdapter(appAdapter);
//        appAdapter.notifyData(list);
        listview.setOnTouchListener((v, event) -> {
            sc = true;
            return false;
        });

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getColor(R.color.colorPrimary));
        onRefresh();

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //判断ListView是否滑动到第一个Item的顶部
                if (mSwipeRefreshLayout != null && listview.getChildCount() > 0 && listview.getFirstVisiblePosition() == 0
                        && listview.getChildAt(0).getTop() >= listview.getPaddingTop()) {
                    //解决滑动冲突，当滑动到第一个item，下拉刷新才起作用
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiblePosition = view.getFirstVisiblePosition();
                if (sc) {
                    recylAppAdapter.setPosition(firstVisiblePosition);
                }
            }
        });

        rightBtn.setOnClickListener(v -> {
//                startActivity(new Intent(mActivity,LeaveActivity.class));
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
    public void getMyAppListSuccess(AppListRsp model) {
        if (model != null && model.getData() != null) {
            List<AppListRsp.DataBean> data = model.getData();
            AppListRsp.DataBean itemBean = new AppListRsp.DataBean();
            itemBean.setAppType("editor");
            itemBean.setName("编辑");
            if (data == null) {
                data = new ArrayList<>();
            }
            data.add(itemBean);
            adapter.notifyData(data);
        }
    }

    @Override
    public void getMyAppFail(String msg) {
//        ToastUtils.showShort(msg);
        Log.d(TAG, "getMyAppFail :" + msg);
    }

    @Override
    public void getAppListSuccess(AppItemBean model) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getMyAppFail :" + model);
        recylAppAdapter.notifydata(model.getData().getRecords());
        appAdapter.notifyData(model.getData().getRecords());
    }

    @Override
    public void getAppListFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getMyAppFail :" + msg);
    }

}
