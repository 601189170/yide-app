package com.yyide.chatim.model.schedule

import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yyide.chatim.utils.DateUtils
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
        const val TYPE_EXPIRED_NOT_COMPLETED: Int = 1
        const val TYPE_EXPIRED_COMPLETED: Int = 2
        const val TYPE_UNEXPIRED_COMPLETED: Int = 3
        const val TYPE_UNEXPIRED_NOT_COMPLETED: Int = 4
        const val TYPE_TIME_AXIS:Int = 5

        const val SCHEDULE_TYPE_SCHEDULE = 1
        const val SCHEDULE_TYPE_SCHOOL_SCHEDULE = 2
        const val SCHEDULE_TYPE_CONFERENCE = 3
        const val SCHEDULE_TYPE_CLASS_SCHEDULE = 4
    }
}

/**
 * 日程月按日分组数据
 * dateTime 分组key
 */
data class DayOfMonth(
    var dateTime: DateTime,
    var scheduleData: ScheduleData
)

data class DayGroupOfMonth(
    var dateTime: DateTime,
    var scheduleDataList: List<ScheduleData>
)