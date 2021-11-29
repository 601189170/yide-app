package com.yyide.chatim.model.schedule

/**
 * @author liu tao
 * @date 2021/11/29 13:39
 * @description 描述
 */
class SchoolSemesterRsp(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: List<DataBean>? = null
) {
    data class DataBean(
        var id: String? = null,
        var schoolYear: String? = null,
        var name: String? = null,
        var startDate: String? = null,
        var endDate: String? = null,
        var schoolId: String? = null,
        var isEnable: Int = 0,
        var delInd: String? = null,
        var createdDateTime: String? = null,
    )
}