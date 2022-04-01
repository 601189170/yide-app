package com.yyide.chatim.activity.attendance.teacher.viewmodel

import android.app.Application
import android.net.wifi.WifiInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.amap.api.location.AMapLocation
import com.amap.api.location.CoordinateConverter
import com.amap.api.location.DPoint
import com.yyide.chatim.SpData
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.attendance.AttendanceNetwork
import com.yyide.chatim.model.UserBean
import com.yyide.chatim.model.attendance.teacher.PunchInfoBean
import com.yyide.chatim.model.attendance.teacher.PunchMessageBean
import com.yyide.chatim.model.attendance.teacher.TeacherAttendanceRuleBean
import com.yyide.chatim.utils.WifiTool
import com.yyide.chatim.utils.logd
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/3/30 16:55
 * @description 教师考勤viewModel
 */
class TeacherAttendanceViewModel(application: Application) : AndroidViewModel(application) {

    val userInfo: UserBean = SpData.getUser()
    
    

    private val punchMessageLiveData = MutableLiveData<PunchMessageBean>()
    val punchMessage = punchMessageLiveData

    private lateinit var attendanceRule: TeacherAttendanceRuleBean

    //打卡类别: 0不可打卡 1 地址 2 wifi 3外勤
    private val punchInfoLivaData = MutableLiveData<PunchInfoBean>()
    var punchInfo = punchInfoLivaData

    val punchTypeNOT = 0
    val punchTypeAddress = 1
    val punchTypeWifi = 2
    val punchTypeFieldwork = 3
    
    var lat: Double = 0.0
    var long: Double = 0.0

    var wifiInfo: WifiInfo? = null

    /**
     * 判断考勤方式
     */
    fun judgePunchFunction(locationInfo: AMapLocation) {

        lat = locationInfo.latitude
        long = locationInfo.longitude

        if (!this::attendanceRule.isInitialized) {
            logd("规则还没获得")
            return
        }

        val info = PunchInfoBean()

        val startLatlng = DPoint()
        startLatlng.latitude = locationInfo.latitude
        startLatlng.longitude = locationInfo.longitude
        for (addressItem in attendanceRule.addressList) {
            val endLatlng = DPoint()
            endLatlng.latitude = addressItem.lat
            endLatlng.longitude = addressItem.geo
            val currentDis = CoordinateConverter.calculateLineDistance(startLatlng, endLatlng)
            if (currentDis <= addressItem.effectiveRange) {
                info.showContent = addressItem.addressName
                info.type = punchTypeAddress
                break
            }
        }
        // 不可以定位打卡 判断能否wifi打卡
        if (info.type == 0) {
            // 判断Wifi打卡
            val wifi = WifiTool.getConnectedWifiInfo(getApplication())
            if (wifi != null) {
                var isFindWifi = false
                for (wifiItem in attendanceRule.wifiList) {
                    if (wifi.bssid.equals(wifiItem.wifiMac)) {
                        info.showContent = wifiItem.wifiName
                        info.type = punchTypeWifi
                        wifiInfo = wifi
                        isFindWifi = true
                        break
                    }
                }
                if (!isFindWifi) {
                    wifiInfo = null
                }
            }
        }


        punchMessage.value?.let {
            if (info.type == 0 && it.canSignInOutside) {
                info.type = punchTypeFieldwork
                info.showContent = locationInfo.address
            }
        }

        if (info.type != punchInfo.value?.type) {
            punchInfoLivaData.value = info
        }

    }

    fun requestPunch() {
        viewModelScope.launch {
            punchInfo.value?.let {
                when (it.type) {
                    punchTypeAddress, punchTypeFieldwork -> {
                        val map = mutableMapOf<String, Any>()
                        map["geo"] = lat
                        map["lat"] = long
                        val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
                        AttendanceNetwork.punchByAddress(body)
                    }
                    punchTypeWifi -> {
                        wifiInfo?.let { info ->
                            val map = mutableMapOf<String, Any>()
                            map["wifiName"] = info.ssid
                            map["wifiMac"] = info.bssid
                            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
                            AttendanceNetwork.punchByWifi(body)
                        }
                    }
                }
            }
        }
    }


    /**
     * 查询打卡信息
     */
    fun queryPunchMessage(
        geo: String = "",
        lat: String = "",
        wifiName: String = "",
        wifiMac: String = ""
    ) {
        val map = mutableMapOf<String, Any>()
        map["geo"] = geo
        map["lat"] = lat
        map["wifiName"] = wifiName
        map["wifiMac"] = wifiMac
        val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
        viewModelScope.launch {
            val queryResult = AttendanceNetwork.requestPunchMessage(body)
            val queryData = queryResult.getOrNull()
            queryData?.let {
                punchMessageLiveData.value = it
            }

        }
    }

    /**
     * 查询考勤规则
     */
    fun queryAttendanceRule() {
        viewModelScope.launch {
            val queryResult = AttendanceNetwork.requestAttendanceRule()
            val queryData = queryResult.getOrNull()
            if (queryData != null) {
                attendanceRule = queryData
            }
        }
    }


}