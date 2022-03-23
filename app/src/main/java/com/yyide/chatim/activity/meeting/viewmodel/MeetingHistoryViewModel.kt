package com.yyide.chatim.activity.meeting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.MeetingListData
import com.yyide.chatim.model.schedule.ScheduleData
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class MeetingHistoryViewModel : BaseViewModel() {

    private val _meetingListDataLiveData = MutableLiveData<Result<MeetingListData>>()

    val meetingListData: LiveData<Result<MeetingListData>> get() = _meetingListDataLiveData

    /**
     * 查询会议列表数据
     * @param size Int
     * @param current Int
     * @param name String
     * @param timeType Int 0：历史 1：今日 2：更多 3：今日以及将来
     */
    fun requestMeetingListData(size: Int, current: Int, name: String, timeType: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["pageNo"] = current
            map["pageSize"] = size
            map["name"] = name
            map["timeType"] = timeType
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.requestMeetingList(body)
            _meetingListDataLiveData.value = result
        }
    }




}