package com.yyide.chatim.model.schedule

import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.model.schedule.Schedule.Companion.SCHOOL_CALENDAR_TYPE_HEAD
import com.yyide.chatim.model.schedule.Schedule.Companion.SCHOOL_CALENDAR_TYPE_ITEM
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime

/**
 * @author liu tao
 * @date 2021/11/29 13:45
 * @description 描述
 */
class SchoolCalendarRsp(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: List<DataBean>? = null
) {
    data class DataBean(
        var startTime: String? = null,
        var semesterId: String? = null,
        var endTime: String? = null,
        var schoolId: String? = null,
        var remark: String? = null,
        var type: Int = 0
    ) : MultiItemEntity,Comparable<DataBean>{
        override val itemType: Int
            get() =
                if (type == 1)
                    SCHOOL_CALENDAR_TYPE_HEAD
                else
                    SCHOOL_CALENDAR_TYPE_ITEM

        override fun compareTo(other: DataBean): Int {
            val dateTime = ScheduleDaoUtil.toDateTime(this.startTime ?: "", "yyyy-MM-dd")
            val dateTimeOther = ScheduleDaoUtil.toDateTime(other.startTime ?: "", "yyyy-MM-dd")
            val compareTo = dateTime.compareTo(dateTimeOther)
            if (compareTo == 0) {
                val dateTime1 = ScheduleDaoUtil.toDateTime(this.endTime ?: "", "yyyy-MM-dd")
                val dateTimeOther1 = ScheduleDaoUtil.toDateTime(other.endTime ?: "", "yyyy-MM-dd")
                return dateTime1.compareTo(dateTimeOther1)
            }
            return compareTo
        }
        //当前校历是否跨天
        val moreDay: Int
            get() = if (!TextUtils.isEmpty(startTime)) {
                val dateTime = ScheduleDaoUtil.toDateTime(this.startTime ?: "", "yyyy-MM-dd")
                    .simplifiedDataTime()
                val dateTime2 = ScheduleDaoUtil.toDateTime(this.endTime ?: "", "yyyy-MM-dd")
                    .simplifiedDataTime()
                if (dateTime == dateTime2) 0 else 1
            } else 0


    }
}