package com.yyide.chatim.home;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.HelpInfoActivity;
import com.yyide.chatim.activity.HelpListActivity;
import com.yyide.chatim.adapter.HelpItemAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.presenter.HelpPresenter;
import com.yyide.chatim.view.HelpView;
import com.yyide.chatim.view.YDVideo;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

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
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HelpItemAdapter(null);
        adapter.setEmptyView(R.layout.empty);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            HelpItemRep.Records.HelpItemBean itemBean = (HelpItemRep.Records.HelpItemBean) adapter.getData().get(position);
            Intent intent = new Intent(mActivity, HelpInfoActivity.class);
            intent.putExtra("itemBean", itemBean);
            startActivity(intent);
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
        if (adapter != null) {
            adapter.stop();
        }
    }

    @Override
    public void getHelpListFail(String msg) {
        swipeRefreshLayout.setRefreshing(false);
        Log.d("getHelpListFail", "getHelpListFail" + msg);
    }

}
