package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField


data class SchoolWeeklyData(
    @JSONField(name = "attend")
    var attend: Attend,
    @JSONField(name = "summary")
    var summary: Summary,
    @JSONField(name = "work")
    var work: Work
)

data class Attend(
    @JSONField(name = "studentAttend")
    var studentAttend: List<StudentAttend>,
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<TeacherAttend>
)

data class Summary(
    @JSONField(name = "attend")
    var attend: String,
    @JSONField(name = "expend")
    var expend: String,
    @JSONField(name = "work")
    var work: String
)

class Work

data class StudentAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
)

data class TeacherAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
)