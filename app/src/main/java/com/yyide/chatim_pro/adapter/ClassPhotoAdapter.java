package com.yyide.chatim_pro.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.ClassesPhotoBannerRsp;
import com.yyide.chatim_pro.model.ClassesPhotoRsp;
import com.yyide.chatim_pro.utils.GlideUtil;
import com.yyide.chatim_pro.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class ClassPhotoAdapter extends LoopPagerAdapter {

    public List<ClassesPhotoRsp.DataBean.AlbumEntityBean> list = new ArrayList<>();

    public ClassPhotoAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private ClassesPhotoRsp.DataBean.AlbumEntityBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(ViewGroup container, final int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_item, null);

        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);

        //GlideUtil.loadImage(view.getContext(), getItem(position).getUrl(), img);
        img.setOnClickListener(v -> {
            if (mItemClickListener != null) mItemClickListener.OnItemClickListener();
        });
        GlideUtil.loadImage(container.getContext(), getItem(position).getUrl(), img);
        return view;
    }

    public interface ItemClickListener {
        void OnItemClickListener();
    }

    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getRealCount() {
        return list != null ? list.size() : 0;
    }

    public void notifyData(List<ClassesPhotoRsp.DataBean.AlbumEntityBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
