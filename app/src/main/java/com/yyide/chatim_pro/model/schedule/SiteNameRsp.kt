package com.yyide.chatim_pro.model.schedule

/**
 * @author liu tao
 * @date 2021/9/23 9:36
 * @description 描述
 */

data class SiteNameRsp(
    var code: Int = 0,
    var success: Boolean = false,
    var msg: String? = null,
    var data: List<DataBean>? = null
) {
    data class DataBean(
        var id: String? = null,
        var name: String? = null,
        var checked: Boolean = false
    )
}