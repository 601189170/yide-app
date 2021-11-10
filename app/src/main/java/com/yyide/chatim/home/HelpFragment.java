package com.yyide.chatim.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.HelpInfoActivity;
import com.yyide.chatim.activity.HelpListActivity;
import com.yyide.chatim.adapter.HelpItemAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.presenter.HelpPresenter;
import com.yyide.chatim.view.HelpView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpFragment extends BaseMvpFragment<HelpPresenter> implements HelpView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rmtext)
    TextView rmtext;
    @BindView(R.id.jjtext)
    TextView jjtext;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tab1)
    FrameLayout tab1;
    @BindView(R.id.tab2)
    FrameLayout tab2;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private View mBaseView;
    private HelpItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.help_layout, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rmtext.setText("视频和文字教程,\n帮助你快速上手");
        jjtext.setText("便捷省心的应用\n操作技巧");
        initAdapter();
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mvpPresenter.getHelpList();
    }

    @Override
    protected HelpPresenter createPresenter() {
        return new HelpPresenter(this);
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new HelpItemAdapter(null);
        recyclerview.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            HelpItemRep.Records.HelpItemBean itemBean = (HelpItemRep.Records.HelpItemBean) adapter.getData().get(position);
            if (itemBean.getItemType() == 1) {
                Intent intent = new Intent(mActivity, HelpInfoActivity.class);
                intent.putExtra("itemBean", itemBean);
                startActivity(intent);
            }
        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(adapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!GSYVideoManager.isFullState(getActivity())) {
                            GSYVideoManager.releaseAllVideos();
                            adapter.notifyItemChanged(position);
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.tab1, R.id.tab2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                startActivity(new Intent(mActivity, HelpListActivity.class));
                break;
            case R.id.tab2:
                Intent intent = new Intent(mActivity, HelpListActivity.class);
                intent.putExtra("helpType", "helpAdvanced");
                startActivity(intent);
                break;
        }
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
    public void getHelpListSuccess(HelpItemRep model) {
        swipeRefreshLayout.setRefreshing(false);
        if (model != null && model.getData() != null) {
            adapter.setList(model.getData().getRecords());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (adapter != null && hidden) {
            GSYVideoManager.onPause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void getHelpListFail(String msg) {
        swipeRefreshLayout.setRefreshing(false);
        Log.d("getHelpListFail", "getHelpListFail" + msg);
    }

}
