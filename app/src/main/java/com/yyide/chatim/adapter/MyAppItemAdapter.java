package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.APPBean;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class MyAppItemAdapter extends BaseAdapter {
//   public List<String> list=new ArrayList<>();
   public List<APPBean> list=new ArrayList<>();


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public APPBean getItem(int position) {
        return list.get(position);
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
        name.setText(getItem(position).name);
        if (getItem(position).id.equals("99")){
            item.setBackground(view.getContext().getResources().getDrawable(R.drawable.icon_bj));
        }

        return view;
    }
    public void notifyData( List<APPBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
