package com.yyide.chatim_pro.model.message

import com.chad.library.adapter.base.entity.node.BaseNode

data class ElternsItem(
    val elternId: String = "",
    val name: String = "",
    val relations: String = ""
) : BaseNode() {
    override val childNode: MutableList<BaseNode>?
        get() = null
}