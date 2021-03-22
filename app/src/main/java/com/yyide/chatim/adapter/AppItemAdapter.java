package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class AppItemAdapter extends BaseAdapter {
//   public List<String> list=new ArrayList<>();
   public String[] list={"sss","sss","sss"};


    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.icon_item, null, false);
        ImageView item = VHUtil.ViewHolder.get(view, R.id.item);
        TextView name = VHUtil.ViewHolder.get(view, R.id.name);
        name.setText(getItem(position));

        return view;
    }
//    public void notifyData( List<String> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }

}
