package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.raphets.roundimageview.RoundImageView;

public class NoteItemAdapter extends BaseMultiItemQuickAdapter<TeacherlistRsp.DataBean.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    public NoteItemAdapter() {
        addItemType(0, R.layout.note_list_item2);
        addItemType(1, R.layout.note_list_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TeacherlistRsp.DataBean.RecordsBean itemBean) {
        switch (holder.getItemViewType()) {
            case 1:
                holder.setText(R.id.item, itemBean.organizationItem != null ? itemBean.organizationItem.name : "");
                break;
            default:
                RoundImageView img = holder.getView(R.id.img);
                View line = holder.getView(R.id.line);
                GlideUtil.loadImageHead(
                        getContext(),
                        itemBean.avatar,
                        img
                );

                if (!TextUtils.isEmpty(itemBean.employeeSubjects)){
                    holder.setText(R.id.name, itemBean.name+"("+(itemBean.employeeSubjects)+")");
                }else {
                    holder.setText(R.id.name, itemBean.name);
                }


                holder.setText(R.id.tv_name_title, StringUtils.subString(itemBean.name, 2));
                break;
        }
    }
}
