package com.yyide.chatim.model.attendance.teacher

data class TeacherAttendanceRuleBean(val personName: String = "",
                                     val groupName: String = "",
                                     val restDay: List<String>?,
                                     val addressList: List<AddressListItem>,
                                     val wifiList: List<WifiListItem>,
                                     val deviceList: List<DeviceListItem>,
                                     val workDay: List<String>?)