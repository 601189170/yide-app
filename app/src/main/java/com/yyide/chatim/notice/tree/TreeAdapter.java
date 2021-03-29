package com.yyide.chatim.notice.tree;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by KaelLi on 2018/11/26.
 */
public class TreeAdapter extends BaseQuickAdapter<TreeItem, BaseViewHolder> implements TreeStateChangeListener {
    private final static int ITEM_STATE_CLOSE = 0;
    private final static int ITEM_STATE_OPEN = 1;
    private List<TreeItem> mList;
    private CheckItemListener mCheckListener;

    public void setOnCheckListener(CheckItemListener mCheckListener) {
        this.mCheckListener = mCheckListener;
    }

    public TreeAdapter(int layoutResId, @Nullable List<TreeItem> data) {
        super(layoutResId, data);
        initList(data, 0);
        this.mList = data;
    }

    private void initList(List<TreeItem> list, int level) {
        if (list == null || list.size() <= 0) return;
        for (TreeItem item: list) {
            item.itemLevel = level;
            if (item.child != null && item.child.size() > 0) {
                initList(item.child, level + 1);
            }
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TreeItem treeItem) {
        baseViewHolder.setText(R.id.tv_notice_scope_name, treeItem.title);
//        if (baseViewHolder.getAdapterPosition() == mList.size() - 1) {
//            baseViewHolder.getView(R.id.vDivider).setVisibility(View.VISIBLE);
//        } else if (mList.get(baseViewHolder.getAdapterPosition() + 1).itemLevel == 0) {
//            baseViewHolder.getView(R.id.vDivider).setVisibility(View.VISIBLE);
//        } else {
//            baseViewHolder.getView(R.id.vDivider).setVisibility(View.INVISIBLE);
//        }

        CheckBox checkBox = baseViewHolder.getView(R.id.cb_check);
        checkBox.setChecked(treeItem.isCheck);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!getRecyclerView().isComputingLayout()){
                    mCheckListener.itemChecked(treeItem, isChecked);
                }
            }
        });

        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (treeItem.itemState == ITEM_STATE_CLOSE) {
                    onOpen(treeItem, baseViewHolder.getAdapterPosition());
                } else {
                    onClose(treeItem, baseViewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public void onOpen(TreeItem treeItem, int position) {
        if (treeItem.child != null && treeItem.child.size() > 0) {
            mList.addAll(position + 1, treeItem.child);
            treeItem.itemState = ITEM_STATE_OPEN;
            notifyItemRangeInserted(position + 1, treeItem.child.size());
            notifyItemChanged(position);
        }
    }

    @Override
    public void onClose(TreeItem treeItem, int position) {
        if (treeItem.child != null && treeItem.child.size() > 0) {
            int nextSameOrHigherLevelNodePosition = mList.size() - 1;
            if (mList.size() > position + 1) {
                for (int i = position + 1; i < mList.size(); i++) {
                    if (mList.get(i).itemLevel <= mList.get(position).itemLevel) {
                        nextSameOrHigherLevelNodePosition = i - 1;
                        break;
                    }
                }
                closeChild(mList.get(position));
                if (nextSameOrHigherLevelNodePosition > position) {
                    mList.subList(position + 1, nextSameOrHigherLevelNodePosition + 1).clear();
                    treeItem.itemState = ITEM_STATE_CLOSE;
                    notifyItemRangeRemoved(position + 1, nextSameOrHigherLevelNodePosition - position);
                    notifyItemChanged(position);
                }
            }
        }
    }

    private void closeChild(TreeItem treeItem) {
        if (treeItem.child != null) {
            for (TreeItem child : treeItem.child) {
                child.itemState = ITEM_STATE_CLOSE;
                closeChild(child);
            }
        }
    }

    public interface CheckItemListener {
        void itemChecked(TreeItem checkBean, boolean isChecked);
    }
}
