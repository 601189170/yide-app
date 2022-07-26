package com.yyide.chatim_pro.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.TeacherlistRsp;
import com.yyide.chatim_pro.utils.GlideUtil;
import com.yyide.chatim_pro.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.raphets.roundimageview.RoundImageView;

public class NoteItemAdapter2 extends BaseMultiItemQuickAdapter<TeacherlistRsp.DataBean.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    public NoteItemAdapter2() {
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
