package com.yyide.chatim.adapter.message

import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.message.NotifyParentActivity
import com.yyide.chatim.databinding.ItemNotifyParentNameBinding
import com.yyide.chatim.model.message.ElternsItem
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.show

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 20:39
 * @description 描述
 */
class ParentItemProvider: BaseNodeProvider() {

    private val list = arrayListOf("其他","父亲","母亲","爷爷","奶奶","外公","外婆")

    override val itemViewType: Int
        get() = NotifyParentActivity.SECOND_TREE
    override val layoutId: Int
        get() = R.layout.item_notify_parent_name


    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val viewBind = ItemNotifyParentNameBinding.bind(helper.itemView)
        val eItem = item as ElternsItem
        viewBind.itemNoticeParentTv.text = eItem.name
        if (eItem.relations.trim().isNotEmpty()) {
            viewBind.itemNoticeParentItemTv.text = list[eItem.relations.toInt()]
        }else{
            viewBind.itemNoticeParentItemTv.text = ""
        }
    }
}