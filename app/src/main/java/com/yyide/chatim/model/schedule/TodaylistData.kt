package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/11/12 18:02
 * @description 描述
 */
data class TodaylistData(
    var thisWeekUndoList: List<ScheduleData>? = null,
    var todayList: List<ScheduleData>? = null
)