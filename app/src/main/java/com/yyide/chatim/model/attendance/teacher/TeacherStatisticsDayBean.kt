package com.yyide.chatim.model.attendance.teacher

data class TeacherStatisticsDayBean(val attendanceSchedulingId: Int = 0,
                                    val attendanceMessage: String = "",
                                    val signRecordList: List<SignTimeItem>?,
                                    val attendanceSchedulingDescription: String = "")