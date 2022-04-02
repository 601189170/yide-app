package com.yyide.chatim.model

import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class OpSubjectData(@SerializedName("list")
                           val list: List<SubjectBean>)