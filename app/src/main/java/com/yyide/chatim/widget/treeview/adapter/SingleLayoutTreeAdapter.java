package com.yyide.chatim.widget.treeview.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.widget.treeview.model.NodeId;
import com.yyide.chatim.widget.treeview.model.TreeNode;
import com.yyide.chatim.widget.treeview.util.DpUtil;
import com.yyide.chatim.widget.treeview.util.TreeDataUtils;

import java.util.List;


public class SingleLayoutTreeAdapter<T extends NodeId> extends BaseQuickAdapter<TreeNode<T>, BaseViewHolder> {

    public interface OnTreeClickedListener<T extends NodeId> {

        void onNodeClicked(View view, TreeNode<T> node, int position);

        void onLeafClicked(View view, TreeNode<T> node, int position);
    }

    private OnTreeClickedListener onTreeClickedListener;

    public SingleLayoutTreeAdapter(int layoutResId, @Nullable final List<TreeNode<T>> dataToBind) {
        super(layoutResId, dataToBind);

        setOnItemClickListener((adapter, view, position) -> {
            TreeNode<T> node = dataToBind.get(position);
            if (!node.isLeaf()) {
                List<TreeNode<T>> children = TreeDataUtils.getNodeChildren(node);

                if (node.isExpand()) {
                    dataToBind.removeAll(children);
                    node.setExpand(false);
                    notifyItemRangeRemoved(position + 1, children.size());
                } else {
                    dataToBind.addAll(position + 1, children);
                    node.setExpand(true);
                    notifyItemRangeInserted(position + 1, children.size());
                }

                if (onTreeClickedListener != null) {
                    onTreeClickedListener.onNodeClicked(view, node, position);
                }
            } else {
                if (onTreeClickedListener != null) {
                    onTreeClickedListener.onLeafClicked(view, node, position);
                }
            }

        });
    }

    @Override
    protected void convert(BaseViewHolder helper, TreeNode<T> item) {
        int level = item.getLevel();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        layoutParams.leftMargin = getTreeNodeMargin() * level;
    }

    public void setOnTreeClickedListener(OnTreeClickedListener onTreeClickedListener) {
        this.onTreeClickedListener = onTreeClickedListener;

    }

    protected int getTreeNodeMargin() {
        return DpUtil.dip2px(getContext(), 20);
    }
}
