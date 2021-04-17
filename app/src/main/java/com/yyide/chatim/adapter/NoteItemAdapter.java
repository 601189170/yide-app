package com.yyide.chatim.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.raphets.roundimageview.RoundImageView;

import java.util.List;

public class NoteItemAdapter extends BaseMultiItemQuickAdapter<listByAppRsp.DataBean.ListBean, BaseViewHolder> {
    public NoteItemAdapter(List<listByAppRsp.DataBean.ListBean> data) {
        super(data);
        addItemType(1, R.layout.note_list_item);
        addItemType(2, R.layout.note_list_item2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, listByAppRsp.DataBean.ListBean itemBean) {
        switch (holder.getItemViewType()) {
            case 1:
                holder.setText(R.id.item, itemBean.name);
                break;
            default:
                RoundImageView img = holder.getView(R.id.img);
                holder.setText(R.id.name, itemBean.name);
                holder.setText(R.id.tv_name_title, StringUtils.subString(itemBean.name, 2));
                break;
        }
    }
}
