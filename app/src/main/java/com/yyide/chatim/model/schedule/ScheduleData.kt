package com.yyide.chatim.model.schedule

import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author liu tao
 * @date 2021/9/23 20:02
 * @description 描述
 */
data class ScheduleData(
    val id: String? = null,
    var name: String? = null,
    var siteId: String? = null,
    var type: String? = null,
    var scheduleStatus: String? = null,
    var isTop: String? = null,
    var remark: String? = null,
    var filePath: String? = null,
    var isRepeat: String? = null,
    var status: String? = null,
    var rrule: String? = null,
    var remindType: String? = null,
    var remindTypeInfo: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var iconImg: String? = null,
    var isAllDay: String? = null,
    var label: List<LabelListRsp.DataBean>? = null,
    var participant: List<ParticipantBean>? = null
) {
    data class ParticipantBean(val userId: String? = null)
}