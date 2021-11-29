package com.yyide.chatim.model.schedule

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yyide.chatim.model.schedule.Schedule.Companion.SCHOOL_CALENDAR_TYPE_HEAD
import com.yyide.chatim.model.schedule.Schedule.Companion.SCHOOL_CALENDAR_TYPE_ITEM

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
    ) : MultiItemEntity {
        override val itemType: Int
            get() =
                if (type == 1)
                    SCHOOL_CALENDAR_TYPE_HEAD
                else
                    SCHOOL_CALENDAR_TYPE_ITEM
    }
}