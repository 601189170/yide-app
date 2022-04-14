package com.yyide.chatim_pro.model.sitetable

/**
 * @author liu tao
 * @date 2021/12/9 11:36
 * @description 描述
 */
data class SiteBuildingsRsp(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: DataBean? = null
) {
    data class DataBean(
        var isExistsSiteKcb: Boolean = false,
        var buildings: List<BuildingsBean>? = null
    ) {
        data class BuildingsBean(
            var children: List<ChildrenBean>? = null,
            var id: String? = null,
            var name: String? = null
        ) {
            class ChildrenBean(
                var id: String? = null,
                var name: String? = null
            )
        }
    }
}