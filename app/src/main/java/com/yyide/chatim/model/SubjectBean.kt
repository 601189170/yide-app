package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class SubjectBean(
        @JSONField(name = "id")
        val id: String,
        @JSONField(name = "name")
        val name: String,
        @JSONField(name = "isDelete")
        val isDelete: String,

        var checked: Boolean = false


)


