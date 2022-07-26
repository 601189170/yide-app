package com.yyide.chatim_pro.activity.attendance.teacher.viewmodel

import android.app.Application
import android.net.wifi.WifiInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.amap.api.location.AMapLocation
import com.amap.api.location.CoordinateConverter
import com.amap.api.location.DPoint
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.attendance.AttendanceNetwork
import com.yyide.chatim_pro.model.UserBean
import com.yyide.chatim_pro.model.attendance.teacher.PunchInfoBean
import com.yyide.chatim_pro.model.attendance.teacher.PunchMessageBean
import com.yyide.chatim_pro.utils.WifiTool
import com.yyide.chatim_pro.utils.logd
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

    private var wifiInfo: WifiInfo? = null

    /**
     * 判断考勤方式
     */
    fun judgePunchFunction(locationInfo: AMapLocation) {

        saveLocationInfo = locationInfo


        if (punchMessage.value == null || punchMessage.value?.getOrNull() == null) {
            return
        }

        // 默认不可以打卡
        val info = PunchInfoBean()

        punchMessage.value?.getOrNull()?.let {
            // 判断Wifi打卡
            if (it.canSignByWifi) {
                val wifi = WifiTool.getConnectedWifiInfo(getApplication())
                if (wifi != null) {
                    for (wifiItem in it.wifiList) {
                        if (wifi.bssid.equals(wifiItem.wifiMac)) {
                            info.showContent = String.format(
                                getApplication<Application>().resources.getString(R.string.attendance_in_range),
                                wifiItem.wifiName
                            )
                            info.type = punchTypeWifi
                            wifiInfo = wifi
                            return@let
                        }
                    }
                }
                wifiInfo = null
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
                    val currentDis = CoordinateConverter.calculateLineDistance(startLatlng, endLatlng)
                    if (currentDis <= addressItem.effectiveRange) {
                        info.showContent = String.format(
                            getApplication<Application>().resources.getString(R.string.attendance_in_range),
                            addressItem.addressName
                        )
                        info.type = punchTypeAddress
                        return@let
                    }
                }
            }

            // 不能打卡
            if (info.type == punchTypeNOT){
                when {
                    // WiFi 定位都不能打卡 判断是否能够外勤打卡
                    it.canSignInOutside ->{
                        info.type = punchTypeFieldwork
                        info.showContent = locationInfo.address
                    }
                    it.canSignByWifi -> {
                        info.showContent = getApplication<Application>().resources.getString(R.string.warn_out_of_wifi)
                    }
                    it.canSignByAddress -> {
                        info.showContent = getApplication<Application>().resources.getString(R.string.warn_out_of_range)
                    }
                    else -> {
                        info.showContent = getApplication<Application>().resources.getString(R.string.warn_not_in)
                    }
                }
            }


        }


        // 打卡类型变化了或者描述变化了
        if (info.type != punchInfo.value?.type || info.showContent != punchInfo.value?.showContent) {
            setPunchInfo(info)
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