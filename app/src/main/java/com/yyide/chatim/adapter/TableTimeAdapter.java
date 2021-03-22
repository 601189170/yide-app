package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.yyide.chatim.R;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class TableTimeAdapter extends BaseAdapter {
   public List<String> list=new ArrayList<>();


    @Override
    public int getCount() {
        return 7;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_item, null, false);
        CheckedTextView day = VHUtil.ViewHolder.get(view, R.id.day);
        CheckedTextView time = VHUtil.ViewHolder.get(view, R.id.time);


        return view;
    }
    public void notifyData( List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
