package com.yyide.chatim_pro.model.gate

/**
 * @author liu tao
 * @date 2021/12/31 10:44
 * @description 场地列表
 */
data class SiteBean(
    var id: String,
    var name: String,
    var children: List<ChildrenBean>
) {
    data class ChildrenBean(
        var id: String,
        var name: String
    )
}