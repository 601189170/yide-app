package com.yyide.chatim.model.gate

/**
 * @author liu tao
 * @date 2021/12/31 10:44
 * @description 场地列表
 */
data class SiteBean(
    var id: String? = null,
    var name: String? = null,
    var children: List<ChildrenBean>? = null
) {
    data class ChildrenBean(
        var id: String? = null,
        var name: String? = null
    )
}