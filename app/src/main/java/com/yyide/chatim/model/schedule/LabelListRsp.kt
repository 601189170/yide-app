package com.yyide.chatim.model.schedule

/**
 * @author liu tao
 * @date 2021/9/22 14:16
 * @description 描述
 */

data class LabelListRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
    val data: List<DataBean>? = null
) {
    data class DataBean(
        val id: String? = null,
        val delInd: Any? = null,
        val createdBy: Any? = null,
        val createdDateTime: Any? = null,
        val updatedBy: Any? = null,
        val updatedDateTime: Any? = null,
        val versionStamp: Any? = null,
        val total: Int = 0,
        val size: Int = 0,
        val current: Int = 0,
        val userId: Any? = null,
        val labelName: String? = null,
        val sort: Int = 0,
        val colorValue: String? = null,
        var checked:Boolean = false
    )
}

/**
 * 新增标签
 */
data class NewLabel(
    val labelName: String,
    val colorValue: String
)

data class OldLabel(
    var id:String,
    val labelName: String,
    val colorValue: String
)