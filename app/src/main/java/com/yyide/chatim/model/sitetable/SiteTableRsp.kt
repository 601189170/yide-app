package com.yyide.chatim.model.sitetable

import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.TimeUtil
import org.joda.time.DateTime

/**
 * @author liu tao
 * @date 2021/12/9 15:09
 * @description 描述
 */
data class SiteTableRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
    val data: DataBean? = null
) {
    data class DataBean(
        var sectionList: SectionListBean? = null,
        var semesterYear: String? = null,
        var semesterYearName: String? = null,
        var thisWeek: Int = 0,
        var thisWeekEndDate: String? = null,
        var thisWeekStartDate: String? = null,
        var timetableList: List<TimetableListBean>? = null,
        var weekList: WeekListBean? = null,
        var weekTotal: Int = 0
    ) {
        data class SectionListBean(
            var afternoonList: List<ListBean>? = null,
            var earlySelfStudyList: List<ListBean>? = null,
            var lateSelfStudyList: List<ListBean>? = null,
            var morningList: List<ListBean>? = null,
            var nightList: List<ListBean>? = null,
        ) {
            data class ListBean(
                var endTime: String? = null,
                var id: String? = null,
                var name: String? = null,
                var periodType: Int = 0,
                var sequence: Int = 0,
                var startTime: String? = null
            )
        }

        data class TimetableListBean (
            var id: String? = null,
            var section:Int = 0,
            var week:Int = 0,
            var subjectName: String? = null,
            var date:String? = null,
            var startTime:String? = null,
            var endTime:String? = null,
        )

        data class WeekListBean(
            var friday: String,
            var monday: String,
            var saturday: String,
            var sunday: String,
            var thursday: String,
            var tuesday: String,
            var wednesday: String
        )
    }
}

fun SiteTableRsp.DataBean.WeekListBean.toWeekDayList(): List<TimeUtil.WeekDay> {
    val weekDayList = mutableListOf<TimeUtil.WeekDay>()
    val mondayWeekDay = TimeUtil.WeekDay()
    val mondayDateTime = ScheduleDaoUtil.toDateTime(monday, "yyyy-MM-dd")
    mondayWeekDay.week = getWeek(mondayDateTime, true)
    mondayWeekDay.day = mondayDateTime.toStringTime("MM/dd")
    mondayWeekDay.dataTime = monday
    weekDayList.add(mondayWeekDay)

    val tuesdayWeekDay = TimeUtil.WeekDay()
    val tuesdayDateTime = ScheduleDaoUtil.toDateTime(tuesday, "yyyy-MM-dd")
    tuesdayWeekDay.week = getWeek(tuesdayDateTime, true)
    tuesdayWeekDay.day = tuesdayDateTime.toStringTime("MM/dd")
    tuesdayWeekDay.dataTime = tuesday
    weekDayList.add(tuesdayWeekDay)

    val wednesdayWeekDay = TimeUtil.WeekDay()
    val wednesdayDateTime = ScheduleDaoUtil.toDateTime(wednesday, "yyyy-MM-dd")
    wednesdayWeekDay.week = getWeek(wednesdayDateTime, true)
    wednesdayWeekDay.day = wednesdayDateTime.toStringTime("MM/dd")
    wednesdayWeekDay.dataTime = wednesday
    weekDayList.add(wednesdayWeekDay)

    val thursdayWeekDay = TimeUtil.WeekDay()
    val thursdayDateTime = ScheduleDaoUtil.toDateTime(thursday, "yyyy-MM-dd")
    thursdayWeekDay.week = getWeek(thursdayDateTime, true)
    thursdayWeekDay.day = thursdayDateTime.toStringTime("MM/dd")
    thursdayWeekDay.dataTime = thursday
    weekDayList.add(thursdayWeekDay)

    val fridayWeekDay = TimeUtil.WeekDay()
    val fridayDateTime = ScheduleDaoUtil.toDateTime(friday, "yyyy-MM-dd")
    fridayWeekDay.week = getWeek(fridayDateTime, true)
    fridayWeekDay.day = fridayDateTime.toStringTime("MM/dd")
    fridayWeekDay.dataTime = friday
    weekDayList.add(fridayWeekDay)

    val saturdayWeekDay = TimeUtil.WeekDay()
    val saturdayDateTime = ScheduleDaoUtil.toDateTime(saturday, "yyyy-MM-dd")
    saturdayWeekDay.week = getWeek(saturdayDateTime, true)
    saturdayWeekDay.day = saturdayDateTime.toStringTime("MM/dd")
    saturdayWeekDay.dataTime = saturday
    weekDayList.add(saturdayWeekDay)

    val sundayWeekDay = TimeUtil.WeekDay()
    val sundayDateTime = ScheduleDaoUtil.toDateTime(sunday, "yyyy-MM-dd")
    sundayWeekDay.week = getWeek(sundayDateTime, true)
    sundayWeekDay.day = sundayDateTime.toStringTime("MM/dd")
    sundayWeekDay.dataTime = sunday
    weekDayList.add(sundayWeekDay)
    return weekDayList
}

fun getWeek(dateTime: DateTime, showToday: Boolean): String {
    val today = DateTime.now().simplifiedDataTime()
    val dayOfWeek = dateTime.dayOfWeek % 7
    if (showToday && today == dateTime.simplifiedDataTime()) {
        return "今日"
    }
    when (dayOfWeek) {
        0 -> return "周日"
        1 -> return "周一"
        2 -> return "周二"
        3 -> return "周三"
        4 -> return "周四"
        5 -> return "周五"
        6 -> return "周六"
    }
    return ""
}