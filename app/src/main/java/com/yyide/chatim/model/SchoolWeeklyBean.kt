package com.yyide.chatim.model

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
    var studentAttend: List<SchoolHomeStudentAttend>?,
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<SchoolHomeTeacherAttend>?
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

data class SchoolHomeStudentAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
)

data class SchoolHomeTeacherAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
)