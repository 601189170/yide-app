package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.schedule.Settings
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.loge
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/9/26 17:25
 * @description 描述
 */
class SettingsViewModel : ViewModel() {
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)

    //课表设置是否打开
    val classScheduleEnable = MutableLiveData<Boolean>()
    val classRemindEnable = MutableLiveData<Boolean>()
    val classRemindTime = MutableLiveData<String?>()
    val homeworkReleaseRemindEnable = MutableLiveData<Boolean>()
    val homeworkReleaseRemindTime = MutableLiveData<String?>()

    //校历设置是否打开
    val schoolCalendarEnable = MutableLiveData<Boolean>()
    val schoolCalendarRemindEnable = MutableLiveData<Boolean>()
    val schoolCalendarRemindTime = MutableLiveData<String?>()

    //会议设置是否打开
    val conferenceEnable = MutableLiveData<Boolean>()
    val conferenceRemindEnable = MutableLiveData<Boolean>()
    val conferenceRemindTime = MutableLiveData<String>()

    //当前时间线是否打开
    val curTimelineEnable = MutableLiveData<Boolean>()

    //历史日程是否显示
    val historyScheduleEnable = MutableLiveData<Boolean>()

    //请求设置数据结果
    private val requestSettingsResult = MutableLiveData<List<Settings.DataBean>?>()
    fun getRequestSettingsResult(): LiveData<List<Settings.DataBean>?> {
        return requestSettingsResult
    }

    private val modifySettingsResult = MutableLiveData<Boolean>()
    fun getModifySettingsResult(): LiveData<Boolean> {
        return modifySettingsResult
    }

    init {
        classScheduleEnable.postValue(true)
        schoolCalendarEnable.postValue(true)
        conferenceEnable.postValue(true)
        curTimelineEnable.postValue(true)
        historyScheduleEnable.postValue(true)
        classRemindEnable.postValue(true)
        homeworkReleaseRemindEnable.postValue(true)
        schoolCalendarRemindEnable.postValue(true)
        conferenceRemindEnable.postValue(true)

        classRemindTime.postValue("0")
        homeworkReleaseRemindTime.postValue("0")
        schoolCalendarRemindTime.postValue("0")
        conferenceRemindTime.postValue("0")
        getSettings()
    }

    fun saveSettings() {
        val requestSettings = requestSettingsResult.value
        requestSettings?.forEach {
            val remindType = it.remindType
            when (remindType) {
                0 -> {
                    it.isOpen = if(classScheduleEnable.value == true) 1 else 0
                }
                1 -> {
                    it.isOpen = if(schoolCalendarEnable.value == true) 1 else 0
                }
                2 -> {
                    it.isOpen = if(curTimelineEnable.value == true) 1 else 0
                }
                3 -> {
                    it.isOpen = if(historyScheduleEnable.value == true) 1 else 0
                }
                4 -> {
                    it.isOpen = if (conferenceEnable.value == true) 1 else 0
                }
                5 -> {

                }
                else -> {
                }
            }

            //设置子集 【0：课程表，1：校历，2：当前时间线标识，3：历史日程，4：会议,5:时区】
            val subList = it.subList
            subList?.forEach {
                val remindType1 = it.remindType
                when (remindType) {
                    0 -> {
                        if (remindType1 == 0) {
                            it.isOpen = if (classRemindEnable.value == true) 1 else 0
                            it.remindTime = classRemindTime.value ?: "0"
                        } else if (remindType1 == 1) {
                            it.isOpen = if (homeworkReleaseRemindEnable.value == true) 1 else 0
                            it.remindTime = homeworkReleaseRemindTime.value ?: "0"
                        }
                    }
                    1 -> {
                        it.isOpen = if (schoolCalendarRemindEnable.value == true) 1 else 0
                        it.remindTime = schoolCalendarRemindTime.value ?: "0"
                    }
                    4 -> {
                        it.isOpen = if (conferenceRemindEnable.value == true) 1 else 0
                        it.remindTime = conferenceRemindTime.value ?: "0"
                    }
                    else -> {
                    }
                }
            }

        }
        val toJSONString = JSON.toJSONString(requestSettings)
        loge("修改日程设置：$toJSONString")
        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        apiStores.saveScheduleSetting(requestBody).enqueue(object : Callback<BaseRsp> {
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                val body = response.body()
                if (body != null && body.code == BaseConstant.REQUEST_SUCCES_0) {
                    modifySettingsResult.postValue(true)
                    return
                }
                modifySettingsResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                modifySettingsResult.postValue(false)
            }
        })
    }

    /**
     * 请求日程设置数据
     */
    fun getSettings() {
        apiStores.scheduleSetting.enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                val body = response.body()
                if (body != null && body.code == BaseConstant.REQUEST_SUCCES_0) {
                    val data = body.data
                    data?.let {
                        it.forEach {
                            val remindType = it.remindType
                            val open = it.isOpen == 1
                            //设置子集 【0：课程表，1：校历，2：当前时间线标识，3：历史日程，4：会议,5:时区】
                            when (remindType) {
                                0 -> {
                                    classScheduleEnable.postValue(open)
                                }
                                1 -> {
                                    schoolCalendarEnable.postValue(open)
                                }
                                2 -> {
                                    curTimelineEnable.postValue(open)
                                }
                                3 -> {
                                    historyScheduleEnable.postValue(open)
                                }
                                4 -> {
                                    conferenceEnable.postValue(open)
                                }
                                else -> {
                                }
                            }
                            //设置子集 【0：课程表，1：校历，2：当前时间线标识，3：历史日程，4：会议,5:时区】
                            val subList = it.subList
                            subList?.forEach {
                                val open1 = it.isOpen == 1
                                val remindType1 = it.remindType
                                val remindTime = it.remindTime?:"0"
                                when (remindType) {
                                    0 -> {
                                        if (remindType1 == 0) {
                                            classRemindEnable.postValue(open1)
                                            classRemindTime.postValue(remindTime)
                                        } else if (remindType1 == 1) {
                                            homeworkReleaseRemindEnable.postValue(open1)
                                            homeworkReleaseRemindTime.postValue(remindTime)
                                        }
                                    }
                                    1 -> {
                                        schoolCalendarRemindEnable.postValue(open1)
                                        schoolCalendarRemindTime.postValue(remindTime)
                                    }
                                    4 -> {
                                        conferenceRemindEnable.postValue(open1)
                                        conferenceRemindTime.postValue(remindTime)
                                    }
                                    else -> {
                                    }
                                }
                            }
                        }
                    }
                    requestSettingsResult.postValue(data)
                    return
                }
                requestSettingsResult.postValue(null)
            }

            override fun onFailure(call: Call<Settings>, t: Throwable) {
                requestSettingsResult.postValue(null)
            }

        })
    }
}