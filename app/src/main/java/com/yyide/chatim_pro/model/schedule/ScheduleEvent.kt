package com.yyide.chatim_pro.model.schedule

/**
 * 日程eventbus event
 */
data class ScheduleEvent(
        var type:Int,
        var result: Boolean
){
    companion object{
        const val NEW_TYPE = 1
    }
}