package com.yyide.chatim.model.message

import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.entity.node.NodeFooterImp


data class NotifyParentBean(
    val className:String = "",
    val name: String = "",
    val children: MutableList<BaseNode>?
) : BaseNode(),NodeFooterImp {
    override val childNode: MutableList<BaseNode>?
        get() = children
    override val footerNode: BaseNode?
        get() = ParentFootBean("test")
}