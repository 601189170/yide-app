package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/10 11:32
 * @description 描述
 */
data class ScheduleInner(
    var day:String,
    var week:String,
    var list:List<Schedule>
)