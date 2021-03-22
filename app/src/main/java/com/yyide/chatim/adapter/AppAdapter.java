package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class AppAdapter extends BaseAdapter {
   public List<String> list=new ArrayList<>();


    @Override
    public int getCount() {
        return list.size();
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_item, null, false);
        TextView title = VHUtil.ViewHolder.get(view, R.id.title);
        GridView grid = VHUtil.ViewHolder.get(view, R.id.grid);
        title.setText(getItem(position));
        AppItemAdapter adapter=new AppItemAdapter();
        grid.setAdapter(adapter);
        return view;
    }
    public void notifyData( List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
