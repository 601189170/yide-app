package com.yyide.chatim.model.attendance.teacher

data class DailyRecordItem(val hasAbsenceFromWork: Boolean = false,
                           val aboutDate: String = "",
                           val hasBeLate: Boolean = false,
                           val hasAskForLeave: Boolean = false,
                           val hasLeaveEarly: Boolean = false)