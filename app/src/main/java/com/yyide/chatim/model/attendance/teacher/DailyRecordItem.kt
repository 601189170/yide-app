package com.yyide.chatim.model.attendance.teacher

data class DailyRecordItem(val hasAbsenceFromWork: Boolean = false,
                           val restDay: Boolean = false,
                           val signTime: List<SignTimeItem>?,
                           val aboutDate: String = "",
                           val dailyRuleId: String = "",
                           val hasRecord: Boolean = false,
                           val hasBeLate: Boolean = false,
                           val dailyRuleDescription: String = "",
                           val hasAskForLeave: Boolean = false,
                           val hasLeaveEarly: Boolean = false)