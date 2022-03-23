package com.yyide.chatim.activity.meeting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.schedule.ScheduleData
import kotlinx.coroutines.launch
import okhttp3.RequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/3/22 18:13
 * @description 会议详情的viewModel
 */
class MeetingDetailViewModel : ViewModel(){

    // 删除会议结果
    private val _meetingDelResultLiveData = MutableLiveData<Boolean>()

    val delResultListData: LiveData<Boolean> get() = _meetingDelResultLiveData

    // 会议详情内容
    private val _meetingDetailLiveData = MutableLiveData<Result<ScheduleData>>()

    val meetingDetailLiveData: LiveData<Result<ScheduleData>> get() = _meetingDetailLiveData

    // 会议id
    var detailID = ""

    /**
     * 会议详情
     */
    fun requestMeetingDetail() {
        viewModelScope.launch {
            val result = NetworkApi.requestMeetingDetail(detailID)
            _meetingDetailLiveData.value = result
        }
    }

    /**
     * 删除会议
     */
    fun requestDeleteMeeting() {
        viewModelScope.launch {
            val result = NetworkApi.requestMeetingDel(detailID)
            val resultData = result.getOrNull()
            _meetingDelResultLiveData.value = resultData != null
        }
    }
}