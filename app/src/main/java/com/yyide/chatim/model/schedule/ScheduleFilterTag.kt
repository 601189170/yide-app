package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/18 14:10
 * @description 描述
 */
data class ScheduleFilterTag(
    //日程搜索过滤条件
    //日程类型集合
    var types:List<Int>,
    //状态
    var status:List<Int>,
    //日期
    var startDate:String,
    var endDate:String,
    //标签集合
    var tags:List<Label>
)

data class TagType(
    //0类型1标签
    var type:Int,
    var label: Label?,
    var scheduleType:Int
)