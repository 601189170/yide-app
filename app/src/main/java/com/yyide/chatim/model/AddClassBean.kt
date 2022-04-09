package com.yyide.chatim.model

import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class AddClassBean (
        var className: String,
        val subjectName: String,
        var classesId: String,
        val timetableId: String,
        val timetableTime: String,
        val subjectId: String
        )


