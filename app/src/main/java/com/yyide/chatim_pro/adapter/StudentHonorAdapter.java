package com.yyide.chatim_pro.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.utils.GlideUtil;
import com.yyide.chatim_pro.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class StudentHonorAdapter extends LoopPagerAdapter {

    public List<String> list = new ArrayList<>();

    public StudentHonorAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private String getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(ViewGroup container, final int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_item, null);

        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);

        img.setOnClickListener(v -> {
            if (mItemClickListener != null) mItemClickListener.OnItemClickListener();
        });
        GlideUtil.loadImage(container.getContext(), getItem(position), img);
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
