package com.yyide.chatim_pro.adapter.message

import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.message.NotifyParentActivity
import com.yyide.chatim_pro.databinding.ItemNotifyParentTopBinding
import com.yyide.chatim_pro.model.message.NotifyParentBean


/**
 *
 * @author shenzhibin
 * @date 2022/4/8 20:35
 * @description 描述
 */
class ParentNodeProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = NotifyParentActivity.FIRST_TREE
    override val layoutId: Int
        get() =  R.layout.item_notify_parent_top

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val viewBind = ItemNotifyParentTopBinding.bind(helper.itemView)
        val parent = item as NotifyParentBean
        viewBind.itemNotifyParentNameTv.text = parent.name
        if (parent.className != "") {
            viewBind.itemNotifyParentClassTv.text = "(${parent.className})"
        }else{
            viewBind.itemNotifyParentClassTv.text = ""
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        getAdapter()?.expandOrCollapse(position)
    }
}