package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class rightData(
        val name: String,
        val classesId: String,
        val subjectId: String

)


