package com.yyide.chatim.model.message

import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *
 * @author shenzhibin
 * @date 2022/4/9 9:38
 * @description 描述
 */
data class ParentFootBean(val content:String):BaseNode() {
    override val childNode: MutableList<BaseNode>?
        get() = null
}
