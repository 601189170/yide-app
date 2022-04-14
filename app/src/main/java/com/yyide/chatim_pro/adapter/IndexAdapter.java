package com.yyide.chatim_pro.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Administrator on 2019/3/29.
 */

public class IndexAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private int index = 0;

    public IndexAdapter() {
        super(R.layout.index_item);
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Object item) {
        TextView item1 = holder.getView(R.id.item1);
        TextView item2 = holder.getView(R.id.item2);
        if (holder.getAbsoluteAdapterPosition() == index) {
            item1.setVisibility(View.GONE);
            item2.setVisibility(View.VISIBLE);
        } else {
            item2.setVisibility(View.GONE);
            item1.setVisibility(View.VISIBLE);
        }
    }
}
