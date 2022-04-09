package com.yyide.chatim.adapter.message

import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemNotifyParentNameBinding
import com.yyide.chatim.utils.remove
import com.yyide.chatim.utils.show

/**
 *
 * @author shenzhibin
 * @date 2022/4/9 9:35
 * @description 描述
 */
class ParentFooterProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 0
    override val layoutId: Int
        get() = R.layout.item_notify_parent_name

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val viewBind = ItemNotifyParentNameBinding.bind(helper.itemView)
        viewBind.itemNoticeParentTv.remove()
        viewBind.itemNoticeParentItemTv.remove()
        viewBind.notifyLine.show()
    }
}