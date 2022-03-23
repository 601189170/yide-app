package com.yyide.chatim.model

import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class MeetingListData(@SerializedName("total")
                           val total: Int = 0,
                           @SerializedName("pageNo")
                           val pageNo: Int = 0,
                           @SerializedName("pageSize")
                           val pageSize: Int = 0,
                           @SerializedName("list")
                           val list: List<ScheduleData>)