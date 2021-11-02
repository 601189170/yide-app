package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.utils.GlideUtil;

/**
 * Created by Administrator on 2019/3/29.
 */

public class MyAppItemAdapter extends BaseQuickAdapter<MyAppListRsp.DataBean, BaseViewHolder> {

    public MyAppItemAdapter() {
        super(R.layout.icon_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, MyAppListRsp.DataBean dataBea) {
        ImageView item = viewHolder.getView(R.id.item);
        TextView name = viewHolder.getView(R.id.name);
        MyAppListRsp.DataBean dataBean = getItem(viewHolder.getAbsoluteAdapterPosition());
        if ("editor".equals(getItem(viewHolder.getAbsoluteAdapterPosition()).getAppType())) {
            item.setImageResource(R.drawable.icon_bj);
        } else {
            if (!TextUtils.isEmpty(dataBean.getImg())) {
                GlideUtil.loadCircleImage(getContext(), dataBean.getImg(), item);
            }
        }
        name.setText(dataBean.getName());
    }
}
