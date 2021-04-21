package com.yyide.chatim.adapter;


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
import com.yyide.chatim.R;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class StudentHonorAdapter extends LoopPagerAdapter {

    public List<String> list = new ArrayList<>();
    Activity context;

    public StudentHonorAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public StudentHonorAdapter(RollPagerView viewPager, Activity context) {
        super(viewPager);
        this.context = context;
    }

    private String getItem(int position) {
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
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(10);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(container.getContext()).load(getItem(position)).apply(options).into(img);
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
        return list.size();
    }

    public void notifyData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
