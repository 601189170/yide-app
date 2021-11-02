package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/10 11:33
 * @description 描述
 */
data class ScheduleOuter(
    var month:String,
    var list: List<ScheduleInner>
)