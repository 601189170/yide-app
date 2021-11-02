package com.yyide.chatim.model.schedule

import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/10/17 13:55
 * @description 描述
 */

data class MonthViewScheduleData(
    var dateTime: DateTime,
    var list:List<DayViewScheduleData>
)

data class DayViewScheduleData(
    var dateTime: DateTime,
    var list:List<ScheduleData>
)