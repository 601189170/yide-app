package com.yyide.chatim_pro.model.schedule

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