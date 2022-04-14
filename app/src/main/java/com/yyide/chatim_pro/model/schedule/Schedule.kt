package com.yyide.chatim_pro.model.schedule

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yyide.chatim_pro.database.ScheduleDaoUtil.toDateTime
import com.yyide.chatim_pro.utils.DateUtils
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/9/10 10:33
 * @description 日程实体
 */
data class Schedule(
    var title: String,//标题
    var content: String,//内容
    var type: Int,//类型1日程2校历3会议4课表
    var startDate: String,//日程开始时间
    var endDate: String,//日程结束时间
    var status: Int//日程完成情况0未完成1已完成
) : MultiItemEntity {
    override val itemType: Int
        get() {
            return if (status == 1) {
                if (DateUtils.dateExpired(endDate)) {
                    TYPE_EXPIRED_COMPLETED
                } else {
                    TYPE_UNEXPIRED_COMPLETED
                }
            } else {
                if (DateUtils.dateExpired(endDate)) {
                    TYPE_EXPIRED_NOT_COMPLETED
                } else {
                    TYPE_UNEXPIRED_NOT_COMPLETED
                }
            }
        }


    companion object {
        //日程列表头显示月份
        const val TYPE_LIST_VIEW_HEAD:Int = 0
        const val TYPE_EXPIRED_NOT_COMPLETED: Int = 1 //过时间未完成
        const val TYPE_EXPIRED_COMPLETED: Int = 2  //过时间完成
        const val TYPE_UNEXPIRED_COMPLETED: Int = 3 //未过期已完成
        const val TYPE_UNEXPIRED_NOT_COMPLETED: Int = 4//未过期未完成
        const val TYPE_TIME_AXIS: Int = 5
        //日程类型【0：校历日程，1：课表日程，2：事务日程 3：会议日程】
        const val SCHEDULE_TYPE_SCHEDULE = 2
        const val SCHEDULE_TYPE_SCHOOL_SCHEDULE = 0
        const val SCHEDULE_TYPE_CONFERENCE = 3
        const val SCHEDULE_TYPE_CLASS_SCHEDULE = 1

        //校历类型
        const val SCHOOL_CALENDAR_TYPE_HEAD = 1
        const val SCHOOL_CALENDAR_TYPE_ITEM = 2
    }
}

/**
 * 日程月按日分组数据
 * dateTime 分组key
 */
data class DayOfMonth(
    var dateTime: DateTime,
    var scheduleData: ScheduleData
) : Comparable<DayOfMonth> {
    override fun compareTo(other: DayOfMonth): Int {
        val compareTo = this.dateTime.compareTo(other.dateTime)
        if (compareTo == 0) {
            val dateTime2 = toDateTime(this.scheduleData.moreDayStartTime)
            val dateTimeOther2 = toDateTime(other.scheduleData.moreDayStartTime)
            val compareTo2 = dateTime2.compareTo(dateTimeOther2)
            if (compareTo2 == 0) {
                val dateTime3 = toDateTime(this.scheduleData.moreDayEndTime)
                val dateTimeOther3 = toDateTime(other.scheduleData.moreDayEndTime)
                return dateTime3.compareTo(dateTimeOther3)
            }
            return compareTo2
        }
        return compareTo
    }

}

data class DayGroupOfMonth(
    var dateTime: DateTime,
    var scheduleDataList: List<ScheduleData>
)