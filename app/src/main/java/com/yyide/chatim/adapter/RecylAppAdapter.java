package com.yyide.chatim.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.AppItemBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import retrofit2.http.PUT;


/**
 * Created by Administrator on 2018/6/15.
 */

public class RecylAppAdapter extends RecyclerView.Adapter<RecylAppAdapter.ViewHolder> {

    public int index;

    public int position = -1;

    public List<AppItemBean.DataBean.RecordsBean> list = new ArrayList<>();

    public AppItemBean.DataBean.RecordsBean getItem(int i) {
        return list.get(i);
    }


    @Override
    public RecylAppAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recy_app_item,
                viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.item = view.findViewById(R.id.item);

        viewHolder.line = view.findViewById(R.id.line);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecylAppAdapter.ViewHolder viewHolder, final int i) {
        if (i == index) {
            viewHolder.item.setChecked(true);
            viewHolder.line.setVisibility(View.VISIBLE);

        } else {
            viewHolder.line.setVisibility(View.GONE);
            viewHolder.item.setChecked(false);
        }
        viewHolder.item.setText(getItem(i).getName());
        viewHolder.itemView.setOnClickListener(v -> {
            position = viewHolder.getAdapterPosition();
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View arg0) {
            super(arg0);
            item = arg0.findViewById(R.id.item);
        }

        CheckedTextView item;
        TextView line;
    }

    public void notifydata(List<AppItemBean.DataBean.RecordsBean> list) {
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
}
