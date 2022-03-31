package com.yyide.chatim.model.attendance.teacher

data class TeacherStatisticsDayBean(val attendanceSchedulingId: Int = 0,
                                    val attendanceMessage: String = "",
                                    val signRecordList: List<SignRecordListItem>?,
                                    val attendanceSchedulingDescription: String = "")