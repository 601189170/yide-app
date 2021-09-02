package com.yyide.chatim.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.yyide.chatim.R;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.view.YDVideo;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Hashtable;
import java.util.List;

public class HelpItemAdapter extends BaseMultiItemQuickAdapter<HelpItemRep.Records.HelpItemBean, BaseViewHolder> implements LoadMoreModule {
    private YDVideo videoView;

    public void stop() {
        if (videoView != null) {
            videoView.onPause();
        }
    }

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
                holder.setText(R.id.title, (holder.getAdapterPosition() + 1) + "." + itemBean.getName())
                        .setText(R.id.info, Html.fromHtml(itemBean.getMessage(), Html.FROM_HTML_MODE_COMPACT, source -> {
                            new Thread(() -> {
                                mDrawable.addLevel(0, 0, getContext().getResources().getDrawable(R.mipmap.ic_launcher));
                                mDrawable.setBounds(0, 0, 0, 0);
//                                Bitmap bitmap;
//                                try {
//                                    bitmap = BitmapFactory.decodeStream(new URL(source).openStream());
//                                    Message msg = handler.obtainMessage();
//                                    msg.what = 1123;
//                                    msg.obj = bitmap;
//                                    handler.sendMessage(msg);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            }).start();
                            return mDrawable;
                        }, null));
                break;
            case 2:
                holder.setText(R.id.title, (holder.getAdapterPosition() + 1) + "." + itemBean.getName());
                YDVideo videoView1 = holder.getView(R.id.videoView);
                videoView = videoView1;
                videoView1.setOnFullscreenButtonClick(() -> {
                    videoView1.startWindowFullscreen(getContext(), false, true);
                });
                //全屏动画
                videoView1.setShowFullAnimation(true);
                //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
                //增加封面
                ImageView imageView = new ImageView(holder.itemView.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(getContext())
//                        .setDefaultRequestOptions(
//                                new RequestOptions()
//                                        .frame(0)
//                                        .centerCrop()
//                        )
//                        .load(itemBean.getVideo())
//                        .into(imageView);
//                videoView1.setThumbImageView(imageView);
                videoView1.setUp(itemBean.getVideo(), true, "");
                break;
        }
    }

    private final LevelListDrawable mDrawable = new LevelListDrawable();

//    // 注意啦，这么写Handler是会造成内存泄漏的，实际项目中不要这么直接用。
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1123) { // 使用1123仅仅是因为在11月23号写的
////                Bitmap bitmap = (Bitmap)msg.obj;
////                BitmapDrawable drawable = new BitmapDrawable(null, bitmap);
////                mDrawable.addLevel(1, 1, drawable);
////                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
////                mDrawable.setLevel(1);
//            }
//        }
//    };
}
