package com.yyide.chatim.adapter.message

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.yyide.chatim.activity.message.NotifyParentActivity
import com.yyide.chatim.model.message.ElternsItem
import com.yyide.chatim.model.message.NotifyParentBean
import com.yyide.chatim.model.message.ParentFootBean

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 19:25
 * @description 描述
 */
class ParentQuickAdapter : BaseNodeAdapter() {

    init {
        // 需要占满一行的，使用此方法（例如section）
        //addFullSpanNodeProvider(ParentNodeProvider())
        addNodeProvider(ParentNodeProvider())
        // 普通的item provider
        addNodeProvider(ParentItemProvider())
        addFooterNodeProvider(ParentFooterProvider())
    }


    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when (data[position]) {
            is NotifyParentBean -> {
                NotifyParentActivity.FIRST_TREE
            }
            is ElternsItem -> {
                NotifyParentActivity.SECOND_TREE
            }
            is ParentFootBean -> {
                0
            }
            else -> -1
        }
    }
}