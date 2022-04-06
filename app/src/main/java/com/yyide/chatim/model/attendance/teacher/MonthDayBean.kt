package com.yyide.chatim.model.attendance.teacher

/**
 *
 * @author shenzhibin
 * @date 2022/4/1 14:04
 * @description 年月日实体类
 */
data class MonthDayBean(var year: Int = 0, var month: Int = 0, var day: Int = 0){
    override fun toString(): String {
        return "$year,$month,$day"
    }
}
