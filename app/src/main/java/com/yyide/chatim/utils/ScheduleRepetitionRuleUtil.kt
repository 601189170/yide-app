package com.yyide.chatim.utils

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.yyide.chatim.model.schedule.Repetition
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 *
 * @author liu tao
 * @date 2021/10/13 17:09
 * @description 计算日程重复规则
 */
object ScheduleRepetitionRuleUtil {

    /**
     * 简化日期 去掉时分秒 如 2021-10-15 00:00:00
     */
    fun DateTime.simplifiedDataTime():DateTime{
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        return DateTime.parse(this.toString("yyyy-MM-dd ")+"00:00:00",dateTimeFormatter)
    }
    /**
     * 返回满足重复规则的日期 yyyy-MM-dd 00:00:00
     */
    fun calculate(startDate: String, endDate: String, rule: MutableMap<String, Any>): List<DateTime>{
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val startDateTime = DateTime.parse(startDate, dateTimeFormatter)
        val endDateTime = DateTime.parse(endDate, dateTimeFormatter)
        val calculate = calculate(startDateTime, endDateTime, rule)
        return calculate.map { DateTime.parse(it.toString("yyyy-MM-dd ")+"00:00:00",dateTimeFormatter) }
    }
    /**
     * 计算日程重复规则
     * 规矩重复规则，计算指定时间内的日期
     * @param startDate 重复开始日期
     * @param endDate 重复结束日期
     * @param rule 重复规则
     */
    fun calculate(startDate: DateTime, endDate: DateTime, rule: MutableMap<String, Any>): List<DateTime> {
        val freq = rule["freq"]
        val interval = rule["interval"]?.toString()?.toInt() ?: 1
        //星期 MO(周一),TU(周二),WE(周三),TH(周四),FR(周五),SA(周六),SU(周日)
        var byweekday = mutableListOf<String>()
        var bymonthday = mutableListOf<String>()
        if (rule["byweekday"] != null){
            try {
                byweekday = JSON.parseArray(rule["byweekday"]?.toString(), String::class.java)
            } catch (e: Exception) {
                loge("${rule["byweekday"]}解析报错：${e.localizedMessage}")
            }

        }
        if (rule["bymonthday"] != null){
            try {
                bymonthday = JSON.parseArray(rule["bymonthday"]?.toString(), String::class.java)
            } catch (e: Exception) {
                loge("${rule["bymonthday"]}解析报错：${e.localizedMessage}")
            }
        }

        when (freq) {
            "daily","DAILY" -> {
                return dailyRule(startDate, endDate, interval)
            }
            "weekly","WEEKLY" -> {
                return weeklyRule(startDate, endDate, interval, byweekday)
            }
            "monthly","MONTHLY" -> {
                return monthlyRule(startDate, endDate, interval, bymonthday)
            }
            "yearly","YEARLY" -> {
                return yearlyRule(startDate, endDate, interval)
            }
            else -> {
            }
        }
        return listOf()
    }

    /**
     * 每日规则
     * {"freq": "daily","interval": "1"}
     */
    private fun dailyRule(startDate: DateTime, endDate: DateTime, interval: Int): List<DateTime> {
        val repetitionDate = mutableListOf<DateTime>()
        var nowDateTime = startDate
        repetitionDate.add(nowDateTime)
        while (true) {
            if (nowDateTime < endDate) {
                repetitionDate.add(nowDateTime)
            } else {
                break
            }
            nowDateTime = nowDateTime.plusDays(interval)
        }
        return repetitionDate
    }

    /**
     * 每周规则
     * {"freq": "weekly","interval": "1","byday":"MO,TU,WE,TH,FR"}
     */
    private fun weeklyRule(startDate: DateTime, endDate: DateTime, interval: Int, byweekday: List<String>): List<DateTime> {
        val repetitionDate = mutableListOf<DateTime>()
        var nowDateTime = startDate
        while (true) {
            if (nowDateTime < endDate) {
                if (byweekday.isNullOrEmpty()) {
                    repetitionDate.add(nowDateTime)
                } else {
                    //有byday SU(周日),MO(周一),TU(周二),WE(周三),TH(周四),FR(周五),SA(周六),
                    val firstDayOfWeek = nowDateTime.minusDays(nowDateTime.dayOfWeek % 7)
                    println("$nowDateTime,本周第一天,$firstDayOfWeek")
                    byweekday.forEach {
                        when (it) {
                            "SU" -> {
                                val dayOfWeek = firstDayOfWeek.plusDays(0)
                                if (dayOfWeek >= startDate && dayOfWeek < endDate) {
                                    repetitionDate.add(dayOfWeek)
                                }
                            }
                            "MO" -> {
                                val dayOfWeek = firstDayOfWeek.plusDays(1)
                                if (dayOfWeek >= startDate && dayOfWeek < endDate) {
                                    repetitionDate.add(dayOfWeek)
                                }
                            }
                            "TU" -> {
                                val dayOfWeek = firstDayOfWeek.plusDays(2)
                                if (dayOfWeek >= startDate && dayOfWeek < endDate) {
                                    repetitionDate.add(dayOfWeek)
                                }
                            }
                            "WE" -> {
                                val dayOfWeek = firstDayOfWeek.plusDays(3)
                                if (dayOfWeek >= startDate && dayOfWeek < endDate) {
                                    repetitionDate.add(dayOfWeek)
                                }
                            }
                            "TH" -> {
                                val dayOfWeek = firstDayOfWeek.plusDays(4)
                                if (dayOfWeek >= startDate && dayOfWeek < endDate) {
                                    repetitionDate.add(dayOfWeek)
                                }
                            }
                            "FR" -> {
                                val dayOfWeek = firstDayOfWeek.plusDays(5)
                                if (dayOfWeek >= startDate && dayOfWeek < endDate) {
                                    repetitionDate.add(dayOfWeek)
                                }
                            }
                            "SA" -> {
                                val dayOfWeek = firstDayOfWeek.plusDays(6)
                                if (dayOfWeek >= startDate && dayOfWeek < endDate) {
                                    repetitionDate.add(dayOfWeek)
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }
            } else {
                break
            }
            nowDateTime = nowDateTime.plusWeeks(interval)
        }
        return repetitionDate
    }

    /**
     * 每月规则
     * {"freq": "monthly","interval": "1","bymonthday":"1,2,3,5"}
     */
    private fun monthlyRule(startDate: DateTime, endDate: DateTime, interval: Int, bymonthday: List<String>): List<DateTime> {
        val repetitionDate = mutableListOf<DateTime>()
        var nowDateTime = startDate
        while (true) {
            if (nowDateTime < endDate) {
                if (bymonthday.isNullOrEmpty()) {
                    repetitionDate.add(nowDateTime)
                } else {
                    //有byday SU(周日),MO(周一),TU(周二),WE(周三),TH(周四),FR(周五),SA(周六),
                    //每个月的1日
                    val firstDayOfMonth = nowDateTime.minusDays(nowDateTime.dayOfMonth - 1)
                    println("$nowDateTime,本月第一天,$firstDayOfMonth")
                    bymonthday.forEach {
                        val dayOfMonth = firstDayOfMonth.plusDays(it.toInt() - 1)
                        if (dayOfMonth >= startDate && dayOfMonth < endDate) {
                            repetitionDate.add(dayOfMonth)
                        }
                    }
                }
            } else {
                break
            }
            nowDateTime = nowDateTime.plusMonths(interval)
        }
        return repetitionDate
    }

    /**
     * 每年规则
     * {"freq": "yearly","interval": "1"}
     */
    private fun yearlyRule(startDate: DateTime, endDate: DateTime, interval: Int): List<DateTime> {
        val repetitionDate = mutableListOf<DateTime>()
        var nowDateTime = startDate
        while (true) {
            if (nowDateTime < endDate) {
                repetitionDate.add(nowDateTime)
            } else {
                break
            }
            nowDateTime = nowDateTime.plusYears(interval)
        }
        return repetitionDate

    }
}