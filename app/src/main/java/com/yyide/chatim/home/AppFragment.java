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
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.activity.AppManagerActivity;
import com.yyide.chatim.adapter.AppAdapter;
import com.yyide.chatim.adapter.MyAppItemAdapter;
import com.yyide.chatim.adapter.RecylAppAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.presenter.AppPresenter;
import com.yyide.chatim.view.AppView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AppFragment extends BaseMvpFragment<AppPresenter> implements AppView {

    @BindView(R.id.mygrid)
    GridView mygrid;
    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.right_btn)
    LinearLayout rightBtn;
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

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MyAppItemAdapter();
        mygrid.setAdapter(adapter);
        mygrid.setOnItemClickListener((parent, view1, position, id) -> {
//            if (adapter.list.get(position).id.equals("99")) {
//                    startActivity(new Intent(mActivity,));
//                    startActivity(new Intent(mActivity, AppManagerActivity.class));
//            } else {
//                startActivity(new Intent(mActivity, LeaveActivity.class));
//            }
        });

        recylAppAdapter = new RecylAppAdapter();
        recy.setAdapter(recylAppAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy.setLayoutManager(linearLayoutManager);
        recylAppAdapter.setOnItemClickListener((view12, position) -> {
            sc = false;
            recylAppAdapter.setPosition(position);
            listview.setSelection(position);
//                listview.setSelectionFromTop(position);
//                listview.smoothScrollToPosition(position);
        });
        appAdapter = new AppAdapter();
        listview.setAdapter(appAdapter);
//        appAdapter.notifyData(list);
        listview.setOnTouchListener((v, event) -> {
            sc = true;
            return false;
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiblePosition = view.getFirstVisiblePosition();
//                view.getF

//                if (scrollPosition != firstVisibleItem) {
//                    adapter.setSelectItem(firstVisibleItem);
//                    mLeft.setSelectionFromTop(firstVisibleItem, 40);
//                    scrollPosition = firstVisibleItem;
//                }
                if (sc) {
                    recylAppAdapter.setPosition(firstVisiblePosition);
                }


//                Log.e("TAG", "firstVisiblePosition: "+ JSON.toJSONString(firstVisiblePosition));

            }
        });

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int firstVisiblePosition = parent.getFirstVisiblePosition();
//                Log.e("TAG", "onItemClick: "+ JSON.toJSONString(firstVisiblePosition));
//            }
//        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mActivity,LeaveActivity.class));
                startActivity(new Intent(mActivity, AppManagerActivity.class));
            }
        });
        mvpPresenter.getMyAppList();
        mvpPresenter.getAppList();
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
            adapter.notifyData(model.getData());
        }
    }

    @Override
    public void getMyAppFail(String msg) {
//        ToastUtils.showShort(msg);
        Log.d(TAG, "getMyAppFail :" + msg);
    }

    @Override
    public void getAppListSuccess(AppItemBean model) {
        Log.d(TAG, "getMyAppFail :" + model);
        recylAppAdapter.notifydata(model.getData().getRecords());
        appAdapter.notifyData(model.getData().getRecords());
    }

    @Override
    public void getAppListFail(String msg) {
        Log.d(TAG, "getMyAppFail :" + msg);
    }
}
