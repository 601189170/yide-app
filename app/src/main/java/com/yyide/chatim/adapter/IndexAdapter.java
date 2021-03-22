package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.yyide.chatim.R;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class IndexAdapter extends BaseAdapter {
   public List<String> list=new ArrayList<>();

    public  int index=-1;
    @Override
    public int getCount() {
        return 3;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.index_item, null, false);
        TextView item1 = VHUtil.ViewHolder.get(view, R.id.item1);
        TextView item2 = VHUtil.ViewHolder.get(view, R.id.item2);
        if (position==index){
            item1.setVisibility(View.GONE);
            item2.setVisibility(View.VISIBLE);
        }else {
            item1.setVisibility(View.VISIBLE);
            item2.setVisibility(View.GONE);
        }

        return view;
    }
    public void notifyData( List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void setIndex( int index) {
        this.index = index;
        notifyDataSetChanged();
    }

}
