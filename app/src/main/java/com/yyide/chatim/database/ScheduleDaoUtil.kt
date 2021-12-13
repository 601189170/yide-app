package com.yyide.chatim.database

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.tencent.mmkv.MMKV
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.SpData
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.model.schedule.DayOfMonth
import com.yyide.chatim.model.schedule.FilterTagCollect
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTimeToMinute
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.loge
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

/**
 *
 * @author liu tao
 * @date 2021/10/15 10:06
 * @description 日程本地数据查询工具
 */
object ScheduleDaoUtil {

    private fun scheduleDao(): ScheduleDao {
        return AppDatabase.getInstance(BaseApplication.getInstance()).scheduleDao()
    }

    fun toDateTime(date: String): DateTime {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        return DateTime.parse(date, dateTimeFormatter)
    }

    fun toDateTime(date: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): DateTime {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern(pattern)
        return DateTime.parse(date, dateTimeFormatter)
    }

    /**
     * 指定日期拼接现在得时间（yyy-MM-dd + HH:mm:ss）
     */
    fun DateTime.dateTimeJointNowTime(): DateTime {
        val nowDateTime = DateTime.now()
        return this.withTime(
            nowDateTime.hourOfDay,
            nowDateTime.minuteOfHour,
            nowDateTime.secondOfMinute,
            0
        )
    }

    /**
     * 比较两个时间的大小 忽略秒
     */
    fun compareDate(startTime: String, endTime: String): Int {
        val simplifiedDataTimeToMinute1 = toDateTime(startTime).simplifiedDataTimeToMinute()
        val simplifiedDataTimeToMinute2 = toDateTime(endTime).simplifiedDataTimeToMinute()
        return simplifiedDataTimeToMinute1.compareTo(simplifiedDataTimeToMinute2)
    }

    /**
     * 比较两个时间的大小 忽略秒
     */
    infix fun String.compareTo(date: String): Int {
        val simplifiedDataTimeToMinute1 = toDateTime(this).simplifiedDataTimeToMinute()
        val simplifiedDataTimeToMinute2 = toDateTime(date).simplifiedDataTimeToMinute()
        return simplifiedDataTimeToMinute1.compareTo(simplifiedDataTimeToMinute2)
    }

    /**
     * 指定时间的最后一天的时间
     */
    fun DateTime.lastTimeOfDay(): DateTime {
        return toDateTime(this.toString("yyyy-MM-dd ") + "23:59:59")
    }

    /**
     * datetime to format string date
     */
    fun DateTime.toStringTime(format: String = ""): String {
        if (!TextUtils.isEmpty(format)) {
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern(format)
            return this.toString(dateTimeFormatter)
        }
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        return this.toString(dateTimeFormatter)
    }

    /**
     * 获取一个指定时间的默认开始时间和结束时间，
     * 根据产品需求  处理规则为  取当前时间的 半个小时整数
     * 比如  15：20  取值  15：30 - 14：00
     * 15：40 取值  16：00 - 16：30
     * 23: 40  取值   日期+1  0：00 -  0：30
     * @return 返回一个开始时间与结束时间相差半小时的日时间段
     */
    fun defaultTwoTimeListOfDateTime(dateTime: DateTime = DateTime.now()): List<DateTime> {
        val interval = 30
        val maxMinute = 60
        val minuteOfHour = dateTime.minuteOfHour
        if (minuteOfHour < interval) {
            val startTime = dateTime.minusMinutes(minuteOfHour).plusMinutes(interval)
            val endTime = startTime.plusMinutes(interval)
            return listOf(
                startTime.simplifiedDataTimeToMinute(),
                endTime.simplifiedDataTimeToMinute()
            )
        } else {
            val startTime = dateTime.plusMinutes(maxMinute - minuteOfHour)
            val endTime = startTime.plusMinutes(interval)
            return listOf(
                startTime.simplifiedDataTimeToMinute(),
                endTime.simplifiedDataTimeToMinute()
            )
        }
    }

    /**
     * 判断当前日程是否当前账户创建的
     */
    fun ScheduleData.promoterSelf(): Boolean {
        val userId = SpData.getUserId()
        if (TextUtils.isEmpty(this.promoter) || TextUtils.isEmpty(userId)) {
            return false
        }
        if (this.promoter == userId) {
            return true
        }
        return false
    }

    /**
     * 今日清单日程列表
     */
    fun todayList(): List<ScheduleData> {
        //今日最后的时间
        val finallyTime = DateTime.now().toString("yyyy-MM-dd ") + "23:59:59"
        val finallyDateTime = toDateTime(finallyTime)
        val startDateBeforeScheduleList = scheduleDao().getStartDateBeforeScheduleList(finallyTime)
        val scheduleDataList = mutableListOf<ScheduleData>()
        scheduleDataList.addAll(startDateBeforeScheduleList.map { it.scheduleWithParticipantAndLabelToScheduleData() })
        //需要的日程数据包括重复日程
        val listAllSchedule = mutableListOf<ScheduleData>()
        scheduleDataList.forEach { schedule ->
            //只显示事务日程的日程清单。
            if (schedule.type.toInt() != Schedule.SCHEDULE_TYPE_SCHEDULE) {
                return@forEach
            }
            val repetitionDate =
                ScheduleRepetitionRuleUtil.calculate(
                    schedule.startTime,
                    finallyTime,
                    schedule.rrule
                )
            repetitionDate.forEach {
                if (it.simplifiedDataTime() == DateTime.now().simplifiedDataTime()) {
                    val newSchedule = schedule.clone() as ScheduleData
                    val toDateTime = toDateTime(newSchedule.startTime)
                    val dataTime = it.withTime(
                        toDateTime.hourOfDay,
                        toDateTime.minuteOfHour,
                        toDateTime.secondOfMinute,
                        0
                    )
                    val toDateTime2 = toDateTime(newSchedule.endTime)
                    var dataTime2 = it.withTime(
                        toDateTime2.hourOfDay,
                        toDateTime2.minuteOfHour,
                        toDateTime2.secondOfMinute,
                        0
                    )
                    //考虑跨天显示
                    if (toDateTime.simplifiedDataTime() != toDateTime2.simplifiedDataTime()) {
                        val year = toDateTime2.year - toDateTime.year
                        val month = toDateTime2.monthOfYear - toDateTime.monthOfYear
                        val day = toDateTime2.dayOfMonth - toDateTime.dayOfMonth
                        dataTime2 = dataTime2.plusYears(year)
                        dataTime2 = dataTime2.plusMonths(month)
                        dataTime2 = dataTime2.plusDays(day)
                    }

                    newSchedule.startTime = dataTime.toString("yyyy-MM-dd HH:mm:ss")
                    newSchedule.endTime = dataTime2.toString("yyyy-MM-dd HH:mm:ss")
                    newSchedule.moreDay = 0
                    newSchedule.moreDayStartTime = newSchedule.startTime
                    newSchedule.moreDayEndTime = newSchedule.endTime
                    listAllSchedule.add(newSchedule)
                } else if (it.simplifiedDataTime() < DateTime.now().simplifiedDataTime()) {
                    //计算不是今日的日程(今日之前)，跨天是否在今日显示
//                    logd("---计算不是今日的日程(今日之前)，跨天是否在今日显示--")
//                    loge(
//                        "dataTime=${it}, schedule=[name=${schedule.name}, date=${schedule.startTime} <-> ${schedule.endTime}]"
//                    )
                    val startTime = toDateTime(schedule.startTime)
                    val endTime = toDateTime(schedule.endTime)
                    val now = DateTime.now().simplifiedDataTime()
                    if (now in startTime..endTime) {
                        schedule.moreDay = 1
                        schedule.moreDayStartTime = schedule.startTime
                        schedule.moreDayEndTime = schedule.endTime
                        listAllSchedule.add(schedule)
                    }
                }
            }
        }
        listAllSchedule.sort()
        //计算跨天日程并切分时间段
      /*  val listAllSchedule2 = mutableListOf<ScheduleData>()
        for (scheduleData in listAllSchedule) {
            val startTime = toDateTime(scheduleData.startTime)
            val endTime = toDateTime(scheduleData.endTime)
            if (startTime.simplifiedDataTime() == endTime.simplifiedDataTime()) {
                //不是跨天的日程
                scheduleData.moreDay = 0
                scheduleData.moreDayStartTime = scheduleData.startTime
                scheduleData.moreDayEndTime = scheduleData.endTime
                listAllSchedule2.add(scheduleData)
            } else {
                //跨天的日程
                val daysBetween = Days.daysBetween(startTime, endTime).days
                loge("$startTime,$endTime 跨天的日程跨天${daysBetween}")
                for (index in 0..daysBetween) {
                    if (index == 0) {
                        //第一天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        scheduleData1.moreDayStartTime = scheduleData1.startTime
                        scheduleData1.moreDayEndTime =
                            toDateTime(scheduleData1.startTime).simplifiedDataTime()
                                .toStringTime("yyyy-MM-dd ") + "23:59:59"
                        if (toDateTime(scheduleData1.startTime).simplifiedDataTime() == finallyDateTime.simplifiedDataTime()){
                            listAllSchedule2.add(scheduleData1)
                            break
                        }

                    } else if (index == daysBetween) {
                        //最后一天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        scheduleData1.moreDayStartTime =
                            toDateTime(scheduleData1.endTime).toStringTime("yyyy-MM-dd ") + "00:00:00"
                        scheduleData1.moreDayEndTime = scheduleData1.endTime
                        if (toDateTime(scheduleData1.endTime).simplifiedDataTime() == finallyDateTime.simplifiedDataTime()) {
                            listAllSchedule2.add(scheduleData1)
                        }
                    } else {
                        //中间的天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        val allDay =
                            toDateTime(scheduleData1.startTime).simplifiedDataTime().plusDays(index)
                        scheduleData1.moreDayStartTime =
                            allDay.toStringTime("yyyy-MM-dd ") + "00:00:00"
                        scheduleData1.moreDayEndTime =
                            allDay.toStringTime("yyyy-MM-dd ") + "23:59:59"
                        if (allDay.simplifiedDataTime() == finallyDateTime.simplifiedDataTime()) {
                            listAllSchedule2.add(scheduleData1)
                            break
                        }
                    }
                }
            }
        }
        listAllSchedule2.sort()*/
        return listAllSchedule
    }

    /**
     * 查询本周未完成日程列表
     */
    fun undoneOfWeek(): List<ScheduleData> {
        val nowDateTime = DateTime.now()
        //本周第一天
        val firstDayOfWeek = nowDateTime.minusDays(nowDateTime.dayOfWeek % 7-1).simplifiedDataTime()
        val lastDayOfWeek =
            nowDateTime.plusDays(7 - nowDateTime.dayOfWeek % 7).simplifiedDataTime()
        //本周最后的时间
        val finallyTime = lastDayOfWeek.toString("yyyy-MM-dd ") + "23:59:59"
        val startDateBeforeScheduleList = scheduleDao().getOneWeekUndoneScheduleList(finallyTime)
        //loge("startDateBeforeScheduleList ${startDateBeforeScheduleList.size}")
        val scheduleDataList = mutableListOf<ScheduleData>()
        scheduleDataList.addAll(startDateBeforeScheduleList.map { it.scheduleWithParticipantAndLabelToScheduleData() })
        //需要的本周重复日程
        val listAllSchedule = mutableListOf<ScheduleData>()
        scheduleDataList.forEach { schedule ->
            //只显示事务日程的日程清单。
            if (schedule.type.toInt() != Schedule.SCHEDULE_TYPE_SCHEDULE) {
                return@forEach
            }
            val repetitionDate = ScheduleRepetitionRuleUtil.calculate(
                schedule.startTime,
                finallyTime,
                schedule.rrule
            )
            repetitionDate.forEach {
                if (it in firstDayOfWeek..lastDayOfWeek) {
                    val newSchedule = schedule.clone() as ScheduleData
                    val toDateTime = toDateTime(newSchedule.startTime)
                    val dataTime = it.withTime(
                        toDateTime.hourOfDay,
                        toDateTime.minuteOfHour,
                        toDateTime.secondOfMinute,
                        0
                    )
                    val toDateTime2 = toDateTime(newSchedule.endTime)
                    var dataTime2 = it.withTime(
                        toDateTime2.hourOfDay,
                        toDateTime2.minuteOfHour,
                        toDateTime2.secondOfMinute,
                        0
                    )
                    //考虑跨天显示
                    if (toDateTime.simplifiedDataTime() != toDateTime2.simplifiedDataTime()) {
                        val year = toDateTime2.year - toDateTime.year
                        val month = toDateTime2.monthOfYear - toDateTime.monthOfYear
                        val day = toDateTime2.dayOfMonth - toDateTime.dayOfMonth
                        dataTime2 = dataTime2.plusYears(year)
                        dataTime2 = dataTime2.plusMonths(month)
                        dataTime2 = dataTime2.plusDays(day)
                    }

                    newSchedule.startTime = dataTime.toString("yyyy-MM-dd HH:mm:ss")
                    newSchedule.endTime = dataTime2.toString("yyyy-MM-dd HH:mm:ss")
                    newSchedule.moreDay = 0
                    newSchedule.moreDayStartTime = newSchedule.startTime
                    newSchedule.moreDayEndTime = newSchedule.endTime
                    listAllSchedule.add(newSchedule)
                } else {
                    logd("---计算不是本周的日程(今日之前)，跨天是否在今日显示--")
                    loge(
                        "dataTime=${it}, schedule=[name=${schedule.name}, date=${schedule.startTime} <-> ${schedule.endTime}]"
                    )
                    val startTime = toDateTime(schedule.startTime).simplifiedDataTime()
                    val endTime = toDateTime(schedule.endTime).simplifiedDataTime()
                    if ((startTime >= firstDayOfWeek && startTime < lastDayOfWeek) || (startTime <= firstDayOfWeek && endTime > firstDayOfWeek)) {
                        schedule.moreDay = 1
                        schedule.moreDayStartTime = schedule.startTime
                        schedule.moreDayEndTime = schedule.endTime
                        listAllSchedule.add(schedule)
                    }
                }
            }
        }
        listAllSchedule.sort()
        //计算跨天日程并切分时间段
        val listAllSchedule2 = mutableListOf<ScheduleData>()
        for (scheduleData in listAllSchedule) {
            val startTime = toDateTime(scheduleData.startTime)
            val endTime = toDateTime(scheduleData.endTime)
            if (startTime.simplifiedDataTime() == endTime.simplifiedDataTime()) {
                //不是跨天的日程
                scheduleData.moreDay = 0
                scheduleData.moreDayStartTime = scheduleData.startTime
                scheduleData.moreDayEndTime = scheduleData.endTime
                listAllSchedule2.add(scheduleData)
            } else {
                //跨天的日程
                val daysBetween = Days.daysBetween(startTime, endTime).days
                loge("$startTime,$endTime 跨天的日程跨天${daysBetween}")
                for (index in 0..daysBetween) {
                    if (index == 0) {
                        //第一天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        scheduleData1.moreDayStartTime = scheduleData1.startTime
                        scheduleData1.moreDayEndTime =
                            toDateTime(scheduleData1.startTime).simplifiedDataTime()
                                .toStringTime("yyyy-MM-dd ") + "23:59:59"
                        listAllSchedule2.add(scheduleData1)
                    } else if (index == daysBetween) {
                        //最后一天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        scheduleData1.moreDayStartTime =
                            toDateTime(scheduleData1.endTime).toStringTime("yyyy-MM-dd ") + "00:00:00"
                        scheduleData1.moreDayEndTime = scheduleData1.endTime
                        if (toDateTime(scheduleData1.endTime).simplifiedDataTime() <= lastDayOfWeek) {
                            listAllSchedule2.add(scheduleData1)
                        }
                    } else {
                        //中间的天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        val allDay =
                            toDateTime(scheduleData1.startTime).simplifiedDataTime().plusDays(index)
                        scheduleData1.moreDayStartTime =
                            allDay.toStringTime("yyyy-MM-dd ") + "00:00:00"
                        scheduleData1.moreDayEndTime =
                            allDay.toStringTime("yyyy-MM-dd ") + "23:59:59"
                        if (allDay.simplifiedDataTime() <= lastDayOfWeek) {
                            listAllSchedule2.add(scheduleData1)
                        }
                    }
                }
            }
        }
        listAllSchedule2.sort()
        return listAllSchedule
    }

    /**
     * 查询指定月的数据
     * @param divideMoreDay 是否拆分跨天日程 默认拆分跨天日程
     * @param moreThanMonth 是否请求当前月及以历史月的日程 默认只请求当前月的日程数据
     */
    fun monthlyList(monthDateTime: DateTime, timeAxisDateTime: DateTime?,divideMoreDay:Boolean = true,moreThanMonth:Boolean = false): List<DayOfMonth> {
        var firstDayOfMonth = monthDateTime.dayOfMonth().withMinimumValue().simplifiedDataTime()
        if (moreThanMonth){
            firstDayOfMonth = toDateTime("2000-01-01 00:00:00").simplifiedDataTime()
        }
        val lastDayOfMonth = monthDateTime.dayOfMonth().withMaximumValue().simplifiedDataTime()
        //本周最后的时间
        val finallyTime = lastDayOfMonth.toString("yyyy-MM-dd ") + "23:59:59"
        val startDateBeforeScheduleList = scheduleDao().getStartDateBeforeScheduleList(finallyTime)
        val scheduleDataList = mutableListOf<ScheduleData>()
        scheduleDataList.addAll(startDateBeforeScheduleList.map { it.scheduleWithParticipantAndLabelToScheduleData() })
        //本月需要的日程数据
        val listAllSchedule = mutableListOf<DayOfMonth>()
        scheduleDataList.forEach { schedule ->
            val repetitionDate = ScheduleRepetitionRuleUtil.calculate(
                schedule.startTime,
                finallyTime,
                schedule.rrule
            )
            repetitionDate.forEach {
                if (it in firstDayOfMonth..lastDayOfMonth) {
                    val newSchedule = schedule.clone() as ScheduleData
                    val toDateTime = toDateTime(newSchedule.startTime)
                    val dataTime = it.withTime(
                        toDateTime.hourOfDay,
                        toDateTime.minuteOfHour,
                        toDateTime.secondOfMinute,
                        0
                    )
                    val toDateTime2 = toDateTime(newSchedule.endTime)
                    var dataTime2 = it.withTime(
                        toDateTime2.hourOfDay,
                        toDateTime2.minuteOfHour,
                        toDateTime2.secondOfMinute,
                        0
                    )
                    //考虑跨天显示
                    if (toDateTime.simplifiedDataTime() != toDateTime2.simplifiedDataTime()) {
                        val year = toDateTime2.year - toDateTime.year
                        val month = toDateTime2.monthOfYear - toDateTime.monthOfYear
                        val day = toDateTime2.dayOfMonth - toDateTime.dayOfMonth
                        dataTime2 = dataTime2.plusYears(year)
                        dataTime2 = dataTime2.plusMonths(month)
                        dataTime2 = dataTime2.plusDays(day)
                    }

                    newSchedule.startTime = dataTime.toString("yyyy-MM-dd HH:mm:ss")
                    newSchedule.moreDayStartTime = newSchedule.startTime
                    newSchedule.endTime = dataTime2.toString("yyyy-MM-dd HH:mm:ss")
                    newSchedule.moreDayEndTime = newSchedule.endTime
                    listAllSchedule.add(DayOfMonth(it, newSchedule))
                } else if(divideMoreDay){
                    //计算跨月的日程，为下个月的显示上个月的数据准备
                    loge("查询${firstDayOfMonth.toStringTime()} ~ ${lastDayOfMonth.toStringTime()}的日程")
                    logd("查询指定月的数据:${schedule.name},${schedule.startTime}~${schedule.endTime},${schedule.rrule}")
                    loge("重复规则日期${it.toStringTime()}")
                    val toDateTime = toDateTime(schedule.endTime).simplifiedDataTime()
                    if (toDateTime in firstDayOfMonth..lastDayOfMonth) {
                        val newSchedule = schedule.clone() as ScheduleData
                        newSchedule.moreDayStartTime =
                            firstDayOfMonth.toString("yyyy-MM-dd ") + "00:00:00"
                        listAllSchedule.add(DayOfMonth(firstDayOfMonth, newSchedule))
                    } else {
                        val newSchedule = schedule.clone() as ScheduleData
                        val toDateTime = toDateTime(newSchedule.startTime)
                        val dataTime = it.withTime(
                            toDateTime.hourOfDay,
                            toDateTime.minuteOfHour,
                            toDateTime.secondOfMinute,
                            0
                        )
                        val toDateTime2 = toDateTime(newSchedule.endTime)
                        var dataTime2 = it.withTime(
                            toDateTime2.hourOfDay,
                            toDateTime2.minuteOfHour,
                            toDateTime2.secondOfMinute,
                            0
                        )
                        //考虑跨天显示
                        if (toDateTime.simplifiedDataTime() != toDateTime2.simplifiedDataTime()) {
                            val year = toDateTime2.year - toDateTime.year
                            val month = toDateTime2.monthOfYear - toDateTime.monthOfYear
                            val day = toDateTime2.dayOfMonth - toDateTime.dayOfMonth
                            dataTime2 = dataTime2.plusYears(year)
                            dataTime2 = dataTime2.plusMonths(month)
                            dataTime2 = dataTime2.plusDays(day)
                        }

                        newSchedule.startTime = dataTime.toString("yyyy-MM-dd HH:mm:ss")
                        newSchedule.moreDayStartTime = newSchedule.startTime
                        newSchedule.endTime = dataTime2.toString("yyyy-MM-dd HH:mm:ss")
                        newSchedule.moreDayEndTime = newSchedule.endTime
                        val startTime = toDateTime(newSchedule.moreDayStartTime)
                        val endTime = toDateTime(newSchedule.endTime)
                        val daysBetween = Days.daysBetween(startTime, endTime).days
                        for (index in 0..daysBetween) {
                            val scheduleData1 = newSchedule.clone() as ScheduleData
                            if (index == 0) {
                                scheduleData1.moreDay = 1
                                scheduleData1.moreDayEndTime =
                                    toDateTime(scheduleData1.moreDayStartTime).simplifiedDataTime()
                                        .toStringTime("yyyy-MM-dd ") + "23:59:59"
                                val simplifiedDataTime =
                                    toDateTime(scheduleData1.moreDayEndTime).simplifiedDataTime()
                                if (simplifiedDataTime in firstDayOfMonth..lastDayOfMonth) {
                                    listAllSchedule.add(
                                        DayOfMonth(
                                            simplifiedDataTime,
                                            scheduleData1
                                        )
                                    )
                                }
                            } else if (index == daysBetween) {
                                scheduleData1.moreDay = 1
                                scheduleData1.moreDayStartTime =
                                    toDateTime(scheduleData1.moreDayEndTime).toStringTime("yyyy-MM-dd ") + "00:00:00"
                                val simplifiedDataTime =
                                    toDateTime(scheduleData1.moreDayStartTime).simplifiedDataTime()
                                if (simplifiedDataTime in firstDayOfMonth..lastDayOfMonth) {
                                    listAllSchedule.add(
                                        DayOfMonth(
                                            simplifiedDataTime,
                                            scheduleData1
                                        )
                                    )
                                }
                            } else {
                                val allDay =
                                    toDateTime(scheduleData1.moreDayStartTime).simplifiedDataTime()
                                        .plusDays(index)
                                scheduleData1.moreDay = 1
                                scheduleData1.moreDayStartTime =
                                    allDay.toStringTime("yyyy-MM-dd ") + "00:00:00"
                                scheduleData1.moreDayEndTime =
                                    allDay.toStringTime("yyyy-MM-dd ") + "23:59:59"
                                if (allDay.simplifiedDataTime() in firstDayOfMonth..lastDayOfMonth) {
                                    listAllSchedule.add(
                                        DayOfMonth(
                                            allDay.simplifiedDataTime(),
                                            scheduleData1
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        loge("listAllSchedule size ${listAllSchedule.size}")
        //计算跨天日程并切分时间段
        val listAllSchedule2 = mutableListOf<DayOfMonth>()
        for (dayOfMonth in listAllSchedule) {
            val dateTime = dayOfMonth.dateTime
            val scheduleData = dayOfMonth.scheduleData
            val startTime = toDateTime(scheduleData.moreDayStartTime)
            val endTime = toDateTime(scheduleData.moreDayEndTime)
            if (startTime.simplifiedDataTime() == endTime.simplifiedDataTime()) {
                //不是跨天的日程
                if (showData(scheduleData.moreDayEndTime)){
                    listAllSchedule2.add(dayOfMonth)
                }
            } else if (divideMoreDay){
                //跨天的日程
                val daysBetween = Days.daysBetween(startTime, endTime).days
                loge("$startTime,$endTime 跨天的日程跨天${daysBetween}")
                for (index in 0..daysBetween) {
                    if (index == 0) {
                        //第一天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        scheduleData1.moreDayEndTime =
                            toDateTime(scheduleData1.moreDayStartTime).simplifiedDataTime()
                                .toStringTime("yyyy-MM-dd ") + "23:59:59"
                        if (showData(scheduleData1.moreDayEndTime) && dateTime <= lastDayOfMonth) {
                            val until = scheduleData1.rrule["until"]
                            if (until != null && !TextUtils.isEmpty(until.toString())) {
                                val untilDateTime = toDateTime(until.toString()).simplifiedDataTime()
                                if (dateTime <= untilDateTime) {
                                    listAllSchedule2.add(DayOfMonth(dateTime, scheduleData1))
                                }
                            } else {
                                listAllSchedule2.add(DayOfMonth(dateTime, scheduleData1))
                            }
                        }
                    } else if (index == daysBetween) {
                        //最后一天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        scheduleData1.moreDayStartTime =
                            toDateTime(scheduleData1.endTime).toStringTime("yyyy-MM-dd ") + "00:00:00"
                        scheduleData1.moreDayEndTime = scheduleData1.endTime
                        val simplifiedDataTime = toDateTime(scheduleData1.endTime).simplifiedDataTime()
                        if (showData(scheduleData1.moreDayEndTime) && simplifiedDataTime <= lastDayOfMonth){
                            val until = scheduleData1.rrule["until"]
                            if (until != null && !TextUtils.isEmpty(until.toString())) {
                                val untilDateTime = toDateTime(until.toString()).simplifiedDataTime()
                                if (simplifiedDataTime <= untilDateTime) {
                                    listAllSchedule2.add(DayOfMonth(simplifiedDataTime, scheduleData1))
                                }
                            } else {
                                listAllSchedule2.add(DayOfMonth(simplifiedDataTime, scheduleData1))
                            }
                        }
                    } else {
                        //中间的天
                        val scheduleData1 = scheduleData.clone() as ScheduleData
                        scheduleData1.moreDay = 1
                        val allDay =
                            toDateTime(scheduleData1.moreDayStartTime).simplifiedDataTime().plusDays(index)
                        scheduleData1.moreDayStartTime =
                            allDay.toStringTime("yyyy-MM-dd ") + "00:00:00"
                        scheduleData1.moreDayEndTime =
                            allDay.toStringTime("yyyy-MM-dd ") + "23:59:59"
                        if (showData(scheduleData1.moreDayEndTime) && allDay <= lastDayOfMonth) {
                            val until = scheduleData1.rrule["until"]
                            if (until != null && !TextUtils.isEmpty(until.toString())) {
                                val untilDateTime = toDateTime(until.toString()).simplifiedDataTime()
                                if (allDay.simplifiedDataTime() <= untilDateTime) {
                                    listAllSchedule2.add(DayOfMonth(allDay.simplifiedDataTime(), scheduleData1))
                                }
                            } else {
                                listAllSchedule2.add(DayOfMonth(allDay.simplifiedDataTime(), scheduleData1))
                            }
                        }
                    }
                }
                //listAllSchedule2.add(it)
            } else {
                if (showData(scheduleData.moreDayEndTime)) {
                    listAllSchedule2.add(dayOfMonth)
                }
            }
        }
        listAllSchedule2.sort()
        timeAxisDateTime?.let {
            for (i in 0 until listAllSchedule2.size - 1) {
                val dateTime1 = listAllSchedule2[i].dateTime.simplifiedDataTime()
                val dateTime2 = listAllSchedule2[i + 1].dateTime.simplifiedDataTime()
                if (i != 0 && i != listAllSchedule2.size - 1 && it in dateTime1..dateTime2) {
                    loge("----找到时间轴的位置----")
                    val scheduleData = ScheduleData()
                    scheduleData.isTimeAxis = true
                    scheduleData.isFirstDayOfMonth = false
                    scheduleData.isMonthHead = false
                    scheduleData.startTime = it.toStringTime()
                    listAllSchedule2.add(i, DayOfMonth(it, scheduleData))
                    return@let
                }
            }
        }
        //listAllSchedule2.sort()
        return listAllSchedule2
    }

    /**
     * 判断是否需要添加历史日程
     */
    private fun showData(date: String): Boolean {
        val dateTime = toDateTime(date).simplifiedDataTimeToMinute()
        val now = DateTime.now().simplifiedDataTimeToMinute()
        val decodeInt = MMKV.defaultMMKV().decodeInt(MMKVConstant.YD_SHOW_HISTORY_SCHEDULE, 0)
        if (decodeInt == 0 && dateTime < now) {
            return false
        }
        return true
    }

    /**
     * 删除一条本地的日程数据
     */
    fun deleteScheduleData(scheduleId: String) {
        scheduleDao().deleteSchedule(scheduleId)
        scheduleDao().deleteParticipant(scheduleId)
        scheduleDao().deleteLabel(scheduleId)
    }

    /**
     * 清空数据 临时
     */
    fun clearAll() {
        scheduleDao().deleteAllSchedule()
        scheduleDao().deleteAllParticipant()
        scheduleDao().deleteAllLabel()
    }

    /**
     * 修改日程完成状态
     */
    fun changeScheduleState(id: String, status: String) {
        if (id.isEmpty() || status.isEmpty()) {
            return
        }
        scheduleDao().changeScheduleState(id, status)
    }

//    /**
//     * 更新日程数据
//     */
//    fun updateScheduleData(scheduleData: ScheduleData){
//        val scheduleWithParticipantAndLabel = scheduleData.scheduleDataToScheduleWithParticipantAndLabel()
//        scheduleDao().updateSchedule(scheduleWithParticipantAndLabel)
//    }
    /**
     * 搜索过滤条件的数据
     * 日程类型【0：校历日程，1：课表日程，2：事务日程, 3：会议日程】
     * name like '%' || :name || '%' and type in (:type) and status in (:status) and label.id in(:labelId) group by schedule.id
     *
     */
    fun filterOfSearchSchedule(filterTagCollect: FilterTagCollect): List<ScheduleData> {
        val toJSONString = JSON.toJSONString(filterTagCollect)
        loge("搜索日程：$toJSONString")
        filterTagCollect.also {
            val searchSchedule: List<ScheduleWithParticipantAndLabel>
            if (filterTagCollect.labelId == null || filterTagCollect.labelId?.isEmpty() == true) {
                searchSchedule = scheduleDao().searchSchedule(
                    filterTagCollect.name ?: "",
                    filterTagCollect.status ?: listOf(0, 1),
                    filterTagCollect.type ?: listOf(2, 0, 3, 1)
                )
            } else {
                searchSchedule = scheduleDao().searchSchedule(
                    filterTagCollect.name ?: "",
                    filterTagCollect.status ?: listOf(0, 1),
                    filterTagCollect.type ?: listOf(2, 0, 3, 1),
                    filterTagCollect.labelId ?: listOf()
                )
            }
            loge("searchSchedule size ${searchSchedule.size}")
            loge("searchSchedule data  $searchSchedule")
            val scheduleDataList = mutableListOf<ScheduleData>()
            scheduleDataList.addAll(searchSchedule.map { it.scheduleWithParticipantAndLabelToScheduleData() })
            if (filterTagCollect.startTime == null) {
                return scheduleDataList
            }
            val startTimeDate = toDateTime(filterTagCollect.startTime ?: "").simplifiedDataTime()
            if (filterTagCollect.endTime == null) {
                //没有截止时间，查询满足条件 和开始时间当天的日程 相当于查询当日日程
                val listAllSchedule = mutableListOf<ScheduleData>()
                val endTimeDate =
                    startTimeDate.simplifiedDataTime().toString("yyyy-MM-dd ") + "23:59:59"
                scheduleDataList.forEach { schedule ->
                    val repetitionDate =
                        ScheduleRepetitionRuleUtil.calculate(
                            schedule.startTime,
                            endTimeDate,
                            schedule.rrule
                        )
                    loge("${it.name} repetitionDate:$repetitionDate")
                    if (repetitionDate.contains(DateTime.now().simplifiedDataTime())) {
                        val newSchedule = schedule.clone() as ScheduleData
                        val toDateTime = toDateTime(newSchedule.startTime)
                        val dataTime = startTimeDate.withTime(
                            toDateTime.hourOfDay,
                            toDateTime.minuteOfHour,
                            toDateTime.secondOfMinute,
                            0
                        )
                        val toDateTime2 = toDateTime(newSchedule.endTime)
                        var dataTime2 = startTimeDate.withTime(
                            toDateTime2.hourOfDay,
                            toDateTime2.minuteOfHour,
                            toDateTime2.secondOfMinute,
                            0
                        )
                        //考虑跨天显示
                        if (toDateTime.simplifiedDataTime() != toDateTime2.simplifiedDataTime()) {
                            val year = toDateTime2.year - toDateTime.year
                            val month = toDateTime2.monthOfYear - toDateTime.monthOfYear
                            val day = toDateTime2.dayOfMonth - toDateTime.dayOfMonth
                            dataTime2 = dataTime2.plusYears(year)
                            dataTime2 = dataTime2.plusMonths(month)
                            dataTime2 = dataTime2.plusDays(day)
                        }
                        newSchedule.startTime = dataTime.toString("yyyy-MM-dd HH:mm:ss")
                        newSchedule.endTime = dataTime2.toString("yyyy-MM-dd HH:mm:ss")
                        listAllSchedule.add(newSchedule)
                    }
                }
                listAllSchedule.sort()
                return listAllSchedule
            }
            //计算开始时间和截止时间满足条件的日程数据
            val endTimeDate = toDateTime(filterTagCollect.endTime ?: "").simplifiedDataTime()
            val finallyTime = endTimeDate.toString("yyyy-MM-dd ") + "23:59:59"
            val listAllSchedule = mutableListOf<ScheduleData>()
            scheduleDataList.forEach { schedule ->
                val repetitionDate = ScheduleRepetitionRuleUtil.calculate(
                    schedule.startTime,
                    finallyTime,
                    schedule.rrule
                )
                repetitionDate.forEach {
                    if (it in startTimeDate..endTimeDate) {
                        val newSchedule = schedule.clone() as ScheduleData
                        val toDateTime = toDateTime(newSchedule.startTime)
                        val dataTime = it.withTime(
                            toDateTime.hourOfDay,
                            toDateTime.minuteOfHour,
                            toDateTime.secondOfMinute,
                            0
                        )
                        val toDateTime2 = toDateTime(newSchedule.endTime)
                        var dataTime2 = it.withTime(
                            toDateTime2.hourOfDay,
                            toDateTime2.minuteOfHour,
                            toDateTime2.secondOfMinute,
                            0
                        )
                        //考虑跨天显示
                        if (toDateTime.simplifiedDataTime() != toDateTime2.simplifiedDataTime()) {
                            val year = toDateTime2.year - toDateTime.year
                            val month = toDateTime2.monthOfYear - toDateTime.monthOfYear
                            val day = toDateTime2.dayOfMonth - toDateTime.dayOfMonth
                            dataTime2 = dataTime2.plusYears(year)
                            dataTime2 = dataTime2.plusMonths(month)
                            dataTime2 = dataTime2.plusDays(day)
                        }

                        newSchedule.startTime = dataTime.toString("yyyy-MM-dd HH:mm:ss")
                        newSchedule.endTime = dataTime2.toString("yyyy-MM-dd HH:mm:ss")
                        listAllSchedule.add(newSchedule)
                    }
                }
            }
            listAllSchedule.sort()
            return scheduleDataList
        }
    }

    /**
     * 保存日程数据到本地
     */
    fun insert(scheduleWithParticipantAndLabel: ScheduleWithParticipantAndLabel) {
        scheduleWithParticipantAndLabel.also {
            scheduleDao().insert(it.schedule, it.participantList, it.labelList)
        }
    }
}