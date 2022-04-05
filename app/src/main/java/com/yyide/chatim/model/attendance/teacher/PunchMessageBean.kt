package com.yyide.chatim.model.attendance.teacher

data class PunchMessageBean(val canSignInOutside: Boolean = false,
                            val canSignByWifi: Boolean = false,
                            val canSignByDevice: Boolean = false,
                            val signMessage: String = "",
                            val signInOrOut: Int = 0,
                            val canSign: Boolean = false,
                            val todayNeedSign: Boolean = false,
                            val personName: String = "",
                            val canSignByFace: Boolean = false,
                            val groupName: String = "",
                            val canSignByAddress: Boolean = false,
                            val signByDevicesMethod: List<Int>?,
                            val signTimeList: List<SignTimeListItem>?,
                            val canSignByBluetooth: Boolean = false,
                            val addressList: List<AddressListItem>,
                            val wifiList: List<WifiListItem>,)