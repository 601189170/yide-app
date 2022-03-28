package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.book.BookPatriarchDetailActivity;
import com.yyide.chatim.activity.book.BookSearchActivity;
import com.yyide.chatim.databinding.ItemNewBookGuardianSearchBinding;
import com.yyide.chatim.model.BookGuardianItem;
import com.yyide.chatim.model.Parent;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.raphets.roundimageview.RoundImageView;

import java.util.List;

public class NoteItemAdapter extends BaseMultiItemQuickAdapter<TeacherlistRsp.DataBean.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    public NoteItemAdapter() {
        addItemType(0, R.layout.note_list_item2);
        addItemType(1, R.layout.note_list_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TeacherlistRsp.DataBean.RecordsBean itemBean) {
        switch (holder.getItemViewType()) {
            case 1:
//                holder.setText(R.id.item, itemBean.organizationItem != null ? itemBean.organizationItem.name : "");
                holder.setText(R.id.item, TextUtils.isEmpty(itemBean.name)?"":itemBean.name);
                break;
            default:
                RoundImageView img = holder.getView(R.id.img);
                RecyclerView recyclerView_guardian = holder.getView(R.id.recyclerView_guardian);
                recyclerView_guardian.setLayoutManager(new LinearLayoutManager(getContext()));
                GuardianAdapter guardianAdapter = new GuardianAdapter();
                recyclerView_guardian.setAdapter(guardianAdapter);
                guardianAdapter.setList(itemBean.elternAddBookDTOList);
                guardianAdapter.setOnItemClickListener((adapter, view, position1) -> {
                    Parent item1 = guardianAdapter.getItem(position1);
//                    if (BookSearchActivity.FROM_GATE.equals(from)){
//                        //跳转闸机通行数据详情页
//                        return;
//                    }
                    BookGuardianItem guardianItem = new BookGuardianItem(
                            item1.getName(),
                            item1.getId(),
                            item1.getPhone(),
                            item1.getUserId(),
                            item1.getRelations(),
                            item1.getWorkUnit(),
                            item1.getFaceInformation(),
                            item1.getSingleParent()
                    );
                    BookPatriarchDetailActivity.start(getContext(), guardianItem);
                });
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
    class GuardianAdapter extends
            BaseQuickAdapter<Parent, BaseViewHolder> {
        public GuardianAdapter() {
            super(R.layout.item_new_book_guardian_search);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, Parent item) {
            ItemNewBookGuardianSearchBinding bind = ItemNewBookGuardianSearchBinding.bind(baseViewHolder.itemView);
            bind.tvName.setText(item.getName());
            switch (item.getRelations()){
                case "0":
                    bind.tvGuardianName.setText("父亲");
                    break;
                case "1":
                    bind.tvGuardianName.setText("母亲");
                    break;
                case "2":
                    bind.tvGuardianName.setText("爷爷");
                    break;
                case "3":
                    bind.tvGuardianName.setText("奶奶");
                    break;
                case "4":
                    bind.tvGuardianName.setText("外公");
                    break;
                case "5":
                    bind.tvGuardianName.setText("外婆");
                    break;
                case "6":
                    bind.tvGuardianName.setText("其他监护人");
                    break;
                default:
                    bind.tvGuardianName.setText("其他监护人");
                    break;
            }



    }
}
}
