package com.yyide.chatim.model

import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class AddClassBean (
        var className: String,
        var subjectName: String,
        var classesId: String,
        var timetableId: String,
        var timetableTime: String,
        var subjectId: String
        )


