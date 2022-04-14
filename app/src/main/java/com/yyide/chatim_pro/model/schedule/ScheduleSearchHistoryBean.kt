package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/11/17 16:29
 * @description 描述
 */
data class ScheduleSearchHistoryBean(
    var userId:String?=null,
    var historyList:MutableList<String> = mutableListOf<String>()
)