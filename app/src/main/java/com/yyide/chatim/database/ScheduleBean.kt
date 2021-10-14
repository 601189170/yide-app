package com.yyide.chatim.database

import androidx.room.*

/**
 *
 * @author liu tao
 * @date 2021/10/14 14:31
 * @description 描述indices = [Index(value = ["date"],unique = true)]
 */

@Entity(tableName = "schedule",indices = [Index(value = ["id"],unique = true)])
data class ScheduleBean(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var id: String? = null,
    var name: String? = null,
    var type: String? = null,
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
    var siteId: String? = null,
    var siteName: String? = null,
    var updateType: String? = null,
    var updateDate: String? = null,
    var dayOfMonth: String? = null,
    var promoterName: String? = null
) {
    @Ignore
    constructor() : this(0)
}

@Entity(tableName = "label")
data class LabelList(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var id: String? = null,
    var labelName: String? = null,
    var colorValue: String? = null,
    var scheduleCreatorId: String? = null
) {
    @Ignore
    constructor() : this(0)
}


@Entity(tableName = "participant")
data class ParticipantList(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var id: String? = null,
    var userId: String? = null,
    var scheduleId: String? = null,
    var type: String? = null,
    var userName: String? = null,
    var scheduleCreatorId: String? = null
) {
    @Ignore
    constructor() : this(0)
}

data class ScheduleWithParticipantAndLabel(
    @Embedded val schedule: ScheduleBean,

    @Relation(
        parentColumn = "id",
        entityColumn = "scheduleCreatorId"
    )
    val participantList: List<ParticipantList>,

    @Relation(
        parentColumn = "id",
        entityColumn = "scheduleCreatorId"
    )
    val labelList: List<LabelList>
)

