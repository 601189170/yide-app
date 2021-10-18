package com.yyide.chatim.activity.meeting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.schedule.ScheduleData
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class MeetingHistoryViewModel : BaseViewModel() {

    val meetingHistoryLiveData = MutableLiveData<Result<List<ScheduleData>>>()

    fun requestMeetingHomeList(size: Int, current: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["size"] = size
            map["current"] = current
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.requestMeetingList(body)
            meetingHistoryLiveData.value = result
        }
    }
}