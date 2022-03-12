package com.yyide.chatim.adapter;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.model.NoteTabBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/6/15.
 */

public class TabRecyAdapter extends RecyclerView.Adapter<TabRecyAdapter.ViewHolder> {

    public int index;

    public int position = -1;

    public List<NoteTabBean> list = new ArrayList<>();

    public NoteTabBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public TabRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_recy_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.item = view.findViewById(R.id.item);
        viewHolder.lastright = view.findViewById(R.id.last_right);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TabRecyAdapter.ViewHolder viewHolder, final int i) {
        if (getItem(i).islast.equals("2")) {
            if (!TextUtils.isEmpty(getItem(i).name)) {
                viewHolder.item.setText(getItem(i).name + " >");
            }
            viewHolder.item.setTextColor(viewHolder.item.getContext().getResources().getColor(R.color.blue11));
            viewHolder.lastright.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(getItem(i).name)) {
                viewHolder.item.setText(getItem(i).name);
            }
            viewHolder.item.setTextColor(viewHolder.item.getContext().getResources().getColor(R.color.black10));
            viewHolder.lastright.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View arg0) {
            super(arg0);
            item = arg0.findViewById(R.id.item);
            lastright = arg0.findViewById(R.id.last_right);
            item.setOnClickListener(this);
        }


        TextView item;
        ImageView lastright;


        @Override
        public void onClick(View v) {
            position = getAdapterPosition();
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
            notifyDataSetChanged();
        }
    }

    public void notifydata(List<NoteTabBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setPosition(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public List<NoteTabBean> remove(int position) {
        List<NoteTabBean> noteTabBeans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (position > i) {
                noteTabBeans.add(list.get(i));
            }
        }
        resetList(noteTabBeans);
        return noteTabBeans;
    }

    public void resetList(List<NoteTabBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
//    List<Integer> removeData=new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//        if (i>position){
//            removeData.add(i);
//        }
//    }
}
