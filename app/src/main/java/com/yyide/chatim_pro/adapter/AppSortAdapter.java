package com.yyide.chatim_pro.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.adapter.helper.ItemTouchHelperAdapter;
import com.yyide.chatim_pro.adapter.helper.ItemTouchHelperViewHolder;
import com.yyide.chatim_pro.model.AppSortParamsBean;
import com.yyide.chatim_pro.model.MyAppListRsp;

import java.util.ArrayList;
import java.util.List;

public class AppSortAdapter extends RecyclerView.Adapter<AppSortAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
    private List<MyAppListRsp.DataBean> mItems;

    public AppSortAdapter(List<MyAppListRsp.DataBean> mItems, OnDragStartListener dragStartListener) {
        mDragStartListener = dragStartListener;
        this.mItems = mItems;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        MyAppListRsp.DataBean prev = mItems.remove(fromPosition);
        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnDragStartListener {
        void onDragStarted(ItemViewHolder holder);
    }

    private final OnDragStartListener mDragStartListener;

    public List<MyAppListRsp.DataBean> getItems(){
        return mItems;
    }

    public List<AppSortParamsBean> getParams(){
        List<AppSortParamsBean> list = new ArrayList<>();
        if(mItems != null){
            for (int i =0; i < mItems.size(); i++){
                MyAppListRsp.DataBean dataBean = mItems.get(i);
                AppSortParamsBean appSortParamsBean = new AppSortParamsBean();
                appSortParamsBean.setId(dataBean.getId());
                appSortParamsBean.setSort(i);
                list.add(appSortParamsBean);
            }
        }
        return list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_sort, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.textView.setText(mItems.get(position).getName());
        if(position == mItems.size() - 1){
            holder.view_line.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnTouchListener((v, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onDragStarted(holder);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final View view_line;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_app_name);
            view_line = (View) itemView.findViewById(R.id.view_line);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
