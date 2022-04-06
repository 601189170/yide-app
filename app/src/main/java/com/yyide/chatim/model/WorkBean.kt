package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class WorkBean(
        @JSONField(name = "isWhole")
        val isWhole: String,
        @JSONField(name = "subjectId")
        val subjectId: String,
        @JSONField(name = "classesId")
        val classesId: String,
        @JSONField(name = "classesId")
        val startTime: String,
        @JSONField(name = "classesId")
        val endTime: String,
        @JSONField(name = "classesId")
        val pageNo: String,
        @JSONField(name = "classesId")
        val pageSize: String
)


