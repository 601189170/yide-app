package com.yyide.chatim.model.sitetable

import com.google.gson.annotations.SerializedName
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.TimeUtil

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
        val jcbMap: JcbMapBean? = null,
        val zxb: ZxbBean? = null,
        val weekmap: WeekmapBean? = null,
        val cdmc: String? = null,
        val list: List<ListBean>? = null,
        val bzzc: String? = null
    ) {
        data class JcbMapBean(
            val sw: List<JcbBean>? = null,
            val wzx: List<JcbBean>? = null,
            val zzx: List<JcbBean>? = null,
            val xw: List<JcbBean>? = null
        ) {
            data class JcbBean(
                val bbh: Int = 0,
                val cjr: String? = null,
                val cjsj: String? = null,
                val gxr: String? = null,
                val gxsj: String? = null,
                val scbs: Int = 0,
                val total: Int = 0,
                val size: Int = 0,
                val current: Int = 0,
                val id: String? = null,
                val xxid: String? = null,
                val zxbid: String? = null,
                val jcdm: Int = 0,
                val jcsd: String? = null,
                val jcsdbs: String? = null,
                val jcmc: String? = null,
                val kssj: String? = null,
                val jssj: String? = null,
                val jcsdAndJcmc: String? = null,
            )
        }

        data class ZxbBean(
            val bbh: Int = 0,
            val cjr: String? = null,
            val cjsj: String? = null,
            val gxr: String? = null,
            val gxsj: String? = null,
            val scbs: Int = 0,
            val total: Int = 0,
            val size: Int = 0,
            val current: Int = 0,
            val id: String? = null,
            val xxid: String? = null,
            val zxbmc: String? = null,
            val zzxkjs: Int = 0,
            val swkjs: Int = 0,
            val xwkjs: Int = 0,
            val wzxkjs: Int = 0,
            val xn: String? = null,
            val xnxq: String? = null,
            val xnxqid: String? = null,
            val xnksrq: String? = null,
            val xnjsrq: String? = null,
            val xnzzc: Int = 0,
            val zzx: List<*>? = null,
            val wzx: List<*>? = null,
            val sw: List<*>? = null,
            val xw: List<*>? = null
        )

        data class WeekmapBean(
            @SerializedName("WEDNESDAY")
            val wednesday: String,

            @SerializedName("MONDAY")
            val monday: String,

            @SerializedName("THURSDAY")
            val thursday: String,

            @SerializedName("SUNDAY")
            val sunday: String,

            @SerializedName("TUESDAY")
            val tuesday: String,

            @SerializedName("FRIDAY")
            val friday: String,

            @SerializedName("SATURDAY")
            val saturday: String,
        )

        data class ListBean(
            val bbh: Any? = null,
            val cjr: Any? = null,
            val cjsj: String? = null,
            val gxr: Any? = null,
            val gxsj: Any? = null,
            val scbs: Int = 0,
            val total: Int = 0,
            val size: Int = 0,
            val current: Int = 0,
            val id: String? = null,
            val xxid: String? = null,
            val xnxqid: String? = null,
            val xnxq: String? = null,
            val kcid: String? = null,
            val kcmc: String? = null,
            val skzc: String? = null,
            val skxq: Int = 0,
            val zxbid: String? = null,
            val jcbid: String? = null,
            val jcdm: Int = 0,
            val jcsd: String? = null,
            val jcmc: String? = null,
            val xh: Int = 0,
            val kssj: String? = null,
            val jssj: String? = null,
            val cdid: String? = null,
            val cdmc: String? = null,
            val jzgid: String? = null,
            val jzgmc: String? = null,
            val bjid: Any? = null,
            val bjmc: Any? = null
        )
    }
}

fun SiteTableRsp.DataBean.WeekmapBean.toWeekDayList(): List<TimeUtil.WeekDay> {
    val weekDayList = mutableListOf<TimeUtil.WeekDay>()
    val mondayWeekDay = TimeUtil.WeekDay()
    val mondayDateTime = ScheduleDaoUtil.toDateTime(monday, "yyyy-MM-dd")
    mondayWeekDay.week = DateUtils.getWeek(mondayDateTime, true)
    mondayWeekDay.day = mondayDateTime.toStringTime("MM/dd")
    mondayWeekDay.dataTime = monday
    weekDayList.add(mondayWeekDay)

    val tuesdayWeekDay = TimeUtil.WeekDay()
    val tuesdayDateTime = ScheduleDaoUtil.toDateTime(tuesday, "yyyy-MM-dd")
    tuesdayWeekDay.week = DateUtils.getWeek(tuesdayDateTime, true)
    tuesdayWeekDay.day = tuesdayDateTime.toStringTime("MM/dd")
    tuesdayWeekDay.dataTime = tuesday
    weekDayList.add(tuesdayWeekDay)

    val wednesdayWeekDay = TimeUtil.WeekDay()
    val wednesdayDateTime = ScheduleDaoUtil.toDateTime(wednesday, "yyyy-MM-dd")
    wednesdayWeekDay.week = DateUtils.getWeek(wednesdayDateTime, true)
    wednesdayWeekDay.day = wednesdayDateTime.toStringTime("MM/dd")
    wednesdayWeekDay.dataTime = wednesday
    weekDayList.add(wednesdayWeekDay)

    val thursdayWeekDay = TimeUtil.WeekDay()
    val thursdayDateTime = ScheduleDaoUtil.toDateTime(thursday, "yyyy-MM-dd")
    thursdayWeekDay.week = DateUtils.getWeek(thursdayDateTime, true)
    thursdayWeekDay.day = thursdayDateTime.toStringTime("MM/dd")
    thursdayWeekDay.dataTime = thursday
    weekDayList.add(thursdayWeekDay)

    val fridayWeekDay = TimeUtil.WeekDay()
    val fridayDateTime = ScheduleDaoUtil.toDateTime(friday, "yyyy-MM-dd")
    fridayWeekDay.week = DateUtils.getWeek(fridayDateTime, true)
    fridayWeekDay.day = fridayDateTime.toStringTime("MM/dd")
    fridayWeekDay.dataTime = friday
    weekDayList.add(fridayWeekDay)

    val saturdayWeekDay = TimeUtil.WeekDay()
    val saturdayDateTime = ScheduleDaoUtil.toDateTime(saturday, "yyyy-MM-dd")
    saturdayWeekDay.week = DateUtils.getWeek(saturdayDateTime, true)
    saturdayWeekDay.day = saturdayDateTime.toStringTime("MM/dd")
    saturdayWeekDay.dataTime = saturday
    weekDayList.add(saturdayWeekDay)

    val sundayWeekDay = TimeUtil.WeekDay()
    val sundayDateTime = ScheduleDaoUtil.toDateTime(sunday, "yyyy-MM-dd")
    sundayWeekDay.week = DateUtils.getWeek(sundayDateTime, true)
    sundayWeekDay.day = sundayDateTime.toStringTime("MM/dd")
    sundayWeekDay.dataTime = sunday
    weekDayList.add(sundayWeekDay)
    return weekDayList
}