package com.yyide.chatim.model.attendance.teacher

data class TeacherPunchMessage(val geo: Double = 0.0,
                               val wifiName: String = "",
                               val wifiMac: String = "",
                               val deviceId: String = "",
                               val lat: Double = 0.0)