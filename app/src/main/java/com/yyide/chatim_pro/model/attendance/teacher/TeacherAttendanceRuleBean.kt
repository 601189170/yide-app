package com.yyide.chatim_pro.model.attendance.teacher

data class TeacherAttendanceRuleBean(
    val restDay: List<String> = mutableListOf(),
    val wifiList: List<WifiListItem> =  mutableListOf(),
    val employeeId: String = "",
    val workDay: List<String> = mutableListOf(),
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
    val addressList: List<AddressListItem> = mutableListOf(),
    val restStart: Int = 0,
    val signTimeList: List<SignTimeListItem> = mutableListOf(),
    val necessaryDate: List<String>?,
    val signInEnd: Int = 0,
    val signInOutside: Boolean = false,
    val timeRange: String = "",
    val hasScheduling: Boolean = false,
    val deviceList:List<DeviceListItem> = mutableListOf()

)