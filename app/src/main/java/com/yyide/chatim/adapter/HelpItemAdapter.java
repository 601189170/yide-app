package com.yyide.chatim.adapter;

import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.view.YDVideo;

import org.jetbrains.annotations.NotNull;

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

    @Override
    protected void convert(@NotNull BaseViewHolder holder, HelpItemRep.Records.HelpItemBean itemBean) {
        switch (holder.getItemViewType()) {
            case 1:
                holder.setText(R.id.title, (holder.getAdapterPosition() + 1) + "." + itemBean.getName()).setText(R.id.info, Html.fromHtml(itemBean.getMessage()));
                break;
            case 2:
                //ImageView imageView = holder.getView(R.id.iv_start);
                holder.setText(R.id.title, (holder.getAdapterPosition() + 1) + "." + itemBean.getName());
                videoView = holder.getView(R.id.videoView);
                //增加封面
                ImageView imageView = new ImageView(holder.itemView.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(holder.itemView.getContext())
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(0)
                                        .centerCrop()
                        )
                        .load(itemBean.getVideo())
                        .into(imageView);
                videoView.setThumbImageView(imageView);
                videoView.setUp(itemBean.getVideo(), false, "");
                break;
        }
    }
}
