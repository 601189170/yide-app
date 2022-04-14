package com.yyide.chatim_pro.model

import com.alibaba.fastjson.annotation.JSONField

data class SchoolWeeklyData(
    @JSONField(name = "attend")
    var attend: SchoolHomeAttendance,
    @JSONField(name = "summary")
    var summary: SchoolHomeSummary,
    @JSONField(name = "work")
    var work: SchoolHomeWork
)

data class SchoolHomeAttendance(
    @JSONField(name = "studentAttend")
    var studentAttend: ArrayList<SchoolHomeAttend>?,
    @JSONField(name = "teacherAttend")
    var teacherAttend: ArrayList<SchoolHomeAttend>?
)

data class SchoolHomeSummary(
    @JSONField(name = "attend")
    var attend: String,
    @JSONField(name = "expend")
    var expend: String,
    @JSONField(name = "work")
    var work: String
)

class SchoolHomeWork(
    @JSONField(name = "name")
    var name: String, @JSONField(name = "value")
    var value: String
)

data class SchoolHomeAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: String,
    @JSONField(name = "rate")
    var rate: Double
)