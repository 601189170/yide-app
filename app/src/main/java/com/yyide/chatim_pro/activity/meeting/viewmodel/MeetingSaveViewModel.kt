package com.yyide.chatim_pro.activity.meeting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.base.BaseViewModel
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.schedule.ParticipantRsp
import com.yyide.chatim_pro.model.schedule.Remind
import com.yyide.chatim_pro.model.schedule.ScheduleData
import com.yyide.chatim_pro.model.schedule.SiteNameRsp
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class MeetingSaveViewModel : BaseViewModel() {

    val meetingSaveLiveData = MutableLiveData<Result<String>>()

    val meetingDelLiveData = MutableLiveData<Result<Boolean>>()

    val meetingDetailLiveData = MutableLiveData<Result<ScheduleData>>()

    //是否是全天日程
    val allDayLiveData = MutableLiveData<Boolean>(false)

    //日程开始时间
    val startTimeLiveData = MutableLiveData<String>()

    //日程结束时间
    val endTimeLiveData = MutableLiveData<String>()

    //场地
    val siteLiveData = MutableLiveData<SiteNameRsp.DataBean>()

    //参与人选择
    val participantList =
        MutableLiveData<List<ParticipantRsp.DataBean.ParticipantListBean>>(mutableListOf())

    //日程id
    val scheduleId = MutableLiveData<String>()

    //日程提醒
    val remindLiveData = MutableLiveData<Remind>()

    /**
     * 创建会议
     */
    fun requestMeetingSave(scheduleData: ScheduleData) {
        viewModelScope.launch {
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(scheduleData))
            val result = NetworkApi.requestMeetingSave(body)
            meetingSaveLiveData.value = result
        }
    }

    /**
     * 会议详情
     */
    fun requestMeetingDetail(id: String) {
        viewModelScope.launch {
            val result = NetworkApi.requestMeetingDetail(id)
            meetingDetailLiveData.value = result
        }
    }

    fun requestDel(scheduleId: String) {
        viewModelScope.launch {
            val result = NetworkApi.requestMeetingDel(scheduleId)
            meetingDelLiveData.value = result
        }
    }
}