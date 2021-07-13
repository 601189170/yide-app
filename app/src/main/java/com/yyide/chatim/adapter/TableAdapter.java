package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class TableAdapter extends BaseAdapter {
   public List<String> list=new ArrayList<>();
    int index=-1;

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
        if (index!=-1){
            if (position==3){
                if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_item2, null, false);
            }else {
                if (view == null)
                    view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_item, null, false);

            }
        }else {
            if (view == null){
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_item, null, false);
            }
        }









        TextView item1 = VHUtil.ViewHolder.get(view, R.id.item1);
        item1.setText(getItem(position));

        if (index!=-1){
            if (position==1||position==2){
//                item1.setVisibility(View.GONE);
                item1.setVisibility(View.VISIBLE);
            }else {
                item1.setVisibility(View.VISIBLE);
            }

        } else {
            item1.setVisibility(View.VISIBLE);
        }

//        if (position%7==3){
//            item1.setVisibility(View.VISIBLE);
//        }else {
//            item1.setVisibility(View.GONE);
//        }


        return view;
    }

    public void notifyData( List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void notifyData( List<String> list,int index) {
        this.list = list;
        this.index = index;
        notifyDataSetChanged();
    }
}
