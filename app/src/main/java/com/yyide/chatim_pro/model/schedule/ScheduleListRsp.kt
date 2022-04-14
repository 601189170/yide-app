package com.yyide.chatim_pro.model.schedule



/**
 * @author liu tao
 * @date 2021/10/13 14:29
 * @description 描述
 */

data class ScheduleListRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
    val data: DataBean? = null
) {
    data class DataBean(
        val scheduleList: List<ScheduleData>? = null
    )
}