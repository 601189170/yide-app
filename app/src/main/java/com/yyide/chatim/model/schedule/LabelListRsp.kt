package com.yyide.chatim.model.schedule

/**
 * @author liu tao
 * @date 2021/9/22 14:16
 * @description 描述
 */

data class LabelListRsp(
    var code: Int = 0,
    var success: Boolean = false,
    var msg: String? = null,
    var data: List<DataBean>? = null
) {
    data class DataBean(
        var id: String? = null,
        var delInd: Any? = null,
        var createdBy: Any? = null,
        var createdDateTime: Any? = null,
        var updatedBy: Any? = null,
        var updatedDateTime: Any? = null,
        var versionStamp: Any? = null,
        var total: Int = 0,
        var size: Int = 0,
        var schoolId: String? = null,
        var current: Int = 0,
        var userId: Any? = null,
        var labelName: String? = null,
        var sort: Int = 0,
        var colorValue: String? = null,
        var checked:Boolean = false
    )
}

/**
 * 新增标签
 */
data class NewLabel(
    var id:String,
    val labelName: String,
    val colorValue: String
)

data class OldLabel(
    var id:String,
    val labelName: String,
    val colorValue: String
)