package com.yyide.chatim.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.ClassesBannerRsp;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Administrator on 2019/3/29.
 */

public class IndexAdapter extends BaseQuickAdapter<ClassesBannerRsp.DataBean, BaseViewHolder> {

    private int index = 0;

    public IndexAdapter() {
        super(R.layout.index_item);
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ClassesBannerRsp.DataBean item) {
        TextView item1 = holder.getView(R.id.item1);
        TextView item2 = holder.getView(R.id.item2);
        if (holder.getAdapterPosition() == index) {
            item1.setVisibility(View.GONE);
            item2.setVisibility(View.VISIBLE);
        } else {
            item2.setVisibility(View.GONE);
            item1.setVisibility(View.VISIBLE);
        }
    }
}
