package com.yyide.chatim_pro.model.attendance.teacher

data class TeacherStatisticsMonthBean(val leaveEarlyCount: Int = 0,
                                      val monthOfYear: String = "",
                                      val absenceFromWorkCount: Int = 0,
                                      val dailyRecord: List<DailyRecordItem>,
                                      val askForLeaveCount: Int = 0,
                                      val beLateCount: Int = 0)