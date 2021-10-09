package com.yyide.chatim.model.schedule

/**
 * @author liu tao
 * @date 2021/10/9 11:15
 * @description 描述
 */
data class TodayListRsp(
    var code: Int = 0,
    var success: Boolean = false,
    var msg: String? = null,
    var data: DataBean? = null
) {


    data class DataBean(
        val thisWeekList: List<ScheduleData>? = null,
        val todayList: List<ScheduleData>? = null
    )
}