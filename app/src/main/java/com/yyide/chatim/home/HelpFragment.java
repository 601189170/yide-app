package com.yyide.chatim.home;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.presenter.HelpPresenter;
import com.yyide.chatim.view.HelpView;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpFragment extends BaseMvpFragment<HelpPresenter> implements HelpView {

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

    private View mBaseView;
    private BaseQuickAdapter<HelpItemRep.Records.HelpItemBean, BaseViewHolder> adapter;

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
        mvpPresenter.getHelpList();
    }

    @Override
    protected HelpPresenter createPresenter() {
        return new HelpPresenter(this);
    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter = new BaseQuickAdapter<HelpItemRep.Records.HelpItemBean, BaseViewHolder>(R.layout.item_help) {
//            @Override
//            protected void convert(@NotNull BaseViewHolder baseViewHolder, HelpItemRep.Records.HelpItemBean itemBean) {
//                if ("0".equals(itemBean.getStatus())) {//富文本
//                    baseViewHolder
//                            .setText(R.id.title, itemBean.getName())
//                            .setText(R.id.info, Html.fromHtml(itemBean.getMessage()));
//
//                } else if ("1".equals(itemBean.getStatus())) {//视频
//                    baseViewHolder.setText(R.id.title, itemBean.getName());
//                }
//
//            }
//        };
        adapter = new HelpListAdapter(null);

        recyclerview.setAdapter(adapter);
        //recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(mActivity, 20)));
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
        if (model != null && model.getData() != null) {
            adapter.setList(model.getData().getRecords());
        }
    }

    @Override
    public void getHelpListFail(String msg) {
        Log.d("getHelpListFail", "getHelpListFail" + msg);
    }

    private class HelpListAdapter extends BaseMultiItemQuickAdapter<HelpItemRep.Records.HelpItemBean, BaseViewHolder> {
        public HelpListAdapter(List<HelpItemRep.Records.HelpItemBean> data) {
            super(data);
            addItemType(1, R.layout.item_help);
            addItemType(2, R.layout.item_help_video);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, HelpItemRep.Records.HelpItemBean itemBean) {
            switch (holder.getItemViewType()) {
                case 1:
                    holder.setText(R.id.title, itemBean.getName()).setText(R.id.info, Html.fromHtml(itemBean.getMessage()));
                    break;
                case 2:
                    ImageView imageView = holder.getView(R.id.iv_start);
                    holder.setText(R.id.title, itemBean.getName());
                    VideoView videoView = holder.getView(R.id.videoView);
                    videoView.setVideoURI(Uri.parse(itemBean.getVideo()));
                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!videoView.isPlaying()) {
                                imageView.setVisibility(View.GONE);
                                videoView.start();
                            } else {
                                imageView.setVisibility(View.VISIBLE);
                                videoView.pause();
                            }
                        }
                    });
                    break;
            }
        }
    }
}
