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
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.attendance.AttendanceNetwork
import com.yyide.chatim.model.UserBean
import com.yyide.chatim.model.attendance.teacher.PunchInfoBean
import com.yyide.chatim.model.attendance.teacher.PunchMessageBean
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

    private val punchMessageLiveData = MutableLiveData<Result<PunchMessageBean>>()
    val punchMessage = punchMessageLiveData

    private val punchResultLiveData = MutableLiveData<Result<String>>()
    val punchResult = punchResultLiveData

    //打卡类别: 0不可打卡 1 地址 2 wifi 3外勤
    private val punchInfoLivaData = MutableLiveData<PunchInfoBean>()
    var punchInfo = punchInfoLivaData

    val punchTypeNOT = 0
    val punchTypeAddress = 1
    val punchTypeWifi = 2
    val punchTypeFieldwork = 3

    var saveLocationInfo: AMapLocation? = null

    var wifiInfo: WifiInfo? = null

    /**
     * 判断考勤方式
     */
    fun judgePunchFunction(locationInfo: AMapLocation) {

        saveLocationInfo = locationInfo


        if (punchMessage.value == null || punchMessage.value?.getOrNull() == null) {
            return
        }

        val info = PunchInfoBean()

        punchMessage.value?.getOrNull()?.let {
            // 判断Wifi打卡
            if (it.canSignByWifi) {
                val wifi = WifiTool.getConnectedWifiInfo(getApplication())
                if (wifi != null) {
                    var isFindWifi = false
                    for (wifiItem in it.wifiList) {
                        if (wifi.bssid.equals(wifiItem.wifiMac)) {
                            info.showContent = String.format(
                                getApplication<Application>().resources.getString(R.string.attendance_in_range),
                                wifiItem.wifiName
                            )
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

            // 不可以wifi打卡 判断能否定位打卡
            if (info.type == punchTypeNOT && it.canSignByAddress) {
                // 判断Wifi打卡
                val startLatlng = DPoint()
                startLatlng.latitude = locationInfo.latitude
                startLatlng.longitude = locationInfo.longitude
                for (addressItem in it.addressList) {
                    val endLatlng = DPoint()
                    endLatlng.latitude = addressItem.lat
                    endLatlng.longitude = addressItem.geo
                    val currentDis =
                        CoordinateConverter.calculateLineDistance(startLatlng, endLatlng)
                    if (currentDis <= addressItem.effectiveRange) {
                        info.showContent = String.format(
                            getApplication<Application>().resources.getString(R.string.attendance_in_range),
                            addressItem.addressName
                        )
                        info.type = punchTypeAddress
                        break
                    }
                }
            }


            if (info.type == punchTypeNOT && it.canSignInOutside) {
                info.type = punchTypeFieldwork
                info.showContent = locationInfo.address
            }

            if (info.type == punchTypeNOT && it.canSignByWifi) {
                info.showContent = getApplication<Application>().resources.getString(R.string.warn_out_of_wifi)
            }
        }


        // 打卡类型变化了
        if (info.type != punchInfo.value?.type ||
            // 如果是外勤打卡并且描述是不一样的
            (info.type == punchTypeFieldwork && info.showContent != punchInfo.value?.showContent)
        ) {
            punchInfoLivaData.value = info
        }

    }


    fun setPunchInfo(update: PunchInfoBean) {
        punchInfoLivaData.value = update
    }

    /**
     * 打卡
     */
    fun requestPunch() {
        viewModelScope.launch {
            punchInfo.value?.let {
                logd("type = ${it.type}")
                when (it.type) {
                    punchTypeAddress -> {
                        val map = mutableMapOf<String, Any>()
                        map["geo"] = saveLocationInfo?.longitude ?: ""
                        map["lat"] = saveLocationInfo?.latitude ?: ""
                        map["addressName"] = saveLocationInfo?.address ?: ""
                        map["face"] = ""
                        val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
                        val result = AttendanceNetwork.punchByAddress(body)
                        punchResultLiveData.value = result
                    }
                    punchTypeFieldwork -> {
                        val map = mutableMapOf<String, Any>()
                        map["geo"] = saveLocationInfo?.longitude ?: ""
                        map["lat"] = saveLocationInfo?.latitude ?: ""
                        map["addressName"] = saveLocationInfo?.address ?: ""
                        map["face"] = ""
                        val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
                        val result = AttendanceNetwork.punchByOutSide(body)
                        punchResultLiveData.value = result
                    }
                    punchTypeWifi -> {
                        wifiInfo?.let { info ->
                            val map = mutableMapOf<String, Any>()
                            map["geo"] = saveLocationInfo?.longitude ?: ""
                            map["lat"] = saveLocationInfo?.latitude ?: ""
                            map["wifiName"] = info.ssid.replace("\"", "")
                            map["wifiMac"] = info.bssid
                            map["face"] = ""
                            map["addressName"] = saveLocationInfo?.address ?: ""
                            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
                            val result = AttendanceNetwork.punchByWifi(body)
                            punchResultLiveData.value = result
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
        geo: Double = 0.0,
        lat: Double = 0.0,
        wifiName: String = "",
        wifiMac: String = ""
    ) {
        val map = mutableMapOf<String, Any>()
        map["geo"] = if (geo == 0.0) saveLocationInfo?.longitude ?: 0.0 else geo
        map["lat"] = if (lat == 0.0) saveLocationInfo?.latitude ?: 0.0 else lat
        map["wifiName"] = wifiName
        map["wifiMac"] = wifiMac
        map["addressName"] = saveLocationInfo?.address ?: ""
        val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
        viewModelScope.launch {
            val queryResult = AttendanceNetwork.requestPunchMessage(body)
            punchMessageLiveData.value = queryResult
        }
    }


}