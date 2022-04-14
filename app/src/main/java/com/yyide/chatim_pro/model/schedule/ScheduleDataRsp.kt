package com.yyide.chatim_pro.model.schedule

/**
 * @author liu tao
 * @date 2021/11/2 18:03
 * @description 描述
 */
data class ScheduleDataRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
    val data: DataBean? = null,
) {
    data class DataBean(
        val timeShow: Int = 0,
        val historyShow: Int = 0,
        val scheduleList: List<ScheduleData>? = null
    )
}