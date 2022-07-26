package com.yyide.chatim_pro.adapter;


import android.app.Activity;
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

public class NoiceAnnounAdapter extends LoopPagerAdapter {

    public List<String> list = new ArrayList<>();
    Activity context;



    public NoiceAnnounAdapter(RollPagerView viewPager) {
        super(viewPager);
    }
    public NoiceAnnounAdapter(RollPagerView viewPager, Activity context) {
        super(viewPager);
        this.context=context;

    }

    private String getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(ViewGroup container, final int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.notice_item, null);

//        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);
//
//        GlideUtil.loadImage(view.getContext(),getItem(position),img);



        return view;
    }



    @Override
    public int getRealCount() {
//        return list.size();
        return 3;
    }

    public void notifyData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
