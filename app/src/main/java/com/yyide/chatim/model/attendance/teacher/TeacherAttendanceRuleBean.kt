package com.yyide.chatim.model.attendance.teacher

data class TeacherAttendanceRuleBean(
    val restDay: List<String>,
    val wifiList: List<WifiListItem>,
    val employeeId: String = "",
    val workDay: List<String>,
    val restEnd: Int = 0,
    val signByAddress: Boolean = false,
    val signByWiFi: Boolean = false,
    val todayNeedSign: Boolean = false,
    val signOutStart: Int = 0,
    val personName: String = "",
    val signByFace: Boolean = false,
    val unnecessaryDate: List<String>,
    val groupName: String = "",
    val signByBluetooth: Boolean = false,
    val signByDevice: Boolean = false,
    val addressList: List<AddressListItem>,
    val restStart: Int = 0,
    val signTimeList: List<SignTimeListItem>,
    val necessaryDate: List<String>?,
    val signInEnd: Int = 0,
    val signInOutside: Boolean = false,
    val timeRange: String = "",
    val hasScheduling: Boolean = false
)