package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class SectionAdapter extends BaseAdapter {
   public List<String> list=new ArrayList<>();

    public int index;
    @Override
    public int getCount() {
//        return list.size();
        return 8;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_item, null, false);
        TextView section = VHUtil.ViewHolder.get(view, R.id.section);
        TextView subject = VHUtil.ViewHolder.get(view, R.id.subject);
        FrameLayout bg = VHUtil.ViewHolder.get(view, R.id.bg);
        subject.setText("语文");
        section.setText("第"+position+"节");
        if (position==index){
            bg.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_section_true));
        }else {
            bg.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_section_nomal));
        }


        return view;
    }
    public void notifyData( List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void setIndex(int index){
        this.index=index;
        notifyDataSetChanged();
    }

}
