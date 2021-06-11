package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.yyide.chatim.R;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class BookAdapter extends BaseAdapter {
    public List<String> list = new ArrayList<>();


    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_item, null, false);
        ImageView item = VHUtil.ViewHolder.get(view, R.id.item);
        GlideUtil.loadImage(view.getContext(), getItem(position), item);
        return view;
    }

    public void notifyData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
