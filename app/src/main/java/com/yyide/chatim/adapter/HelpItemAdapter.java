package com.yyide.chatim.adapter;

import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yyide.chatim.R;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.SampleCoverVideo;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpItemAdapter extends BaseMultiItemQuickAdapter<HelpItemRep.Records.HelpItemBean, BaseViewHolder> implements LoadMoreModule {
    public final static String TAG = "RecyclerView2List";

    public HelpItemAdapter(List<HelpItemRep.Records.HelpItemBean> data) {
        super(data);
        addItemType(1, R.layout.item_help);
        addItemType(2, R.layout.item_help_video);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void convert(@NotNull BaseViewHolder holder, HelpItemRep.Records.HelpItemBean itemBean) {
        switch (holder.getItemViewType()) {
            case 1:
                holder.setText(R.id.title, (holder.getAbsoluteAdapterPosition() + 1) + "." + itemBean.getName())
                        .setText(R.id.info, Html.fromHtml(itemBean.getMessage(), Html.FROM_HTML_MODE_COMPACT, source -> {
                            new Thread(() -> {
                                mDrawable.addLevel(0, 0, getContext().getResources().getDrawable(R.mipmap.ic_launcher_logo));
                                mDrawable.setBounds(0, 0, 0, 0);
                            }).start();
                            return mDrawable;
                        }, null));
                break;
            case 2:
                GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
                SampleCoverVideo gsyVideoPlayer = holder.getView(R.id.videoView);
                holder.setText(R.id.title, (holder.getAbsoluteAdapterPosition() + 1) + "." + itemBean.getName());
                Map<String, String> header = new HashMap<>();
                header.put("ee", "33");
                //防止错位，离开释放
                //gsyVideoPlayer.initUIState();
                gsyVideoOptionBuilder
                        .setIsTouchWiget(false)
                        //.setThumbImageView(imageView)
                        .setUrl(itemBean.getVideo())
                        .setVideoTitle(itemBean.getName())
                        .setCacheWithPlay(false)
                        .setRotateViewAuto(true)
                        .setLockLand(true)
                        .setPlayTag(TAG)
                        .setMapHeadData(header)
                        .setShowFullAnimation(true)
                        .setNeedLockFull(true)
                        .setPlayPosition(holder.getAbsoluteAdapterPosition())
                        .setVideoAllCallBack(new GSYSampleCallBack() {
                            @Override
                            public void onPrepared(String url, Object... objects) {
                                super.onPrepared(url, objects);
                                if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                                    //静音
//                        GSYVideoManager.instance().setNeedMute(true);
                                }
                            }

                            @Override
                            public void onQuitFullscreen(String url, Object... objects) {
                                super.onQuitFullscreen(url, objects);
                                //全屏不静音
//                    GSYVideoManager.instance().setNeedMute(true);
                            }

                            @Override
                            public void onEnterFullscreen(String url, Object... objects) {
                                super.onEnterFullscreen(url, objects);
                                //GSYVideoManager.instance().setNeedMute(false);
                                gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                            }
                        }).build(gsyVideoPlayer);

                //增加title
                gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
                //设置封面
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideUtil.loadCover(getContext(), itemBean.getVideo() + "?x-oss-process=video/snapshot,t_1000,m_fast", imageView);
                gsyVideoPlayer.setThumbImageView(imageView);
                //设置返回键
                gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
                //设置全屏按键功能
                gsyVideoPlayer.getFullscreenButton().setOnClickListener(v -> resolveFullBtn(gsyVideoPlayer));
                break;
        }
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(getContext(), true, true);
    }

    private final LevelListDrawable mDrawable = new LevelListDrawable();
}
