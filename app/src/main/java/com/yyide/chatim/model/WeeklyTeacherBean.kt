package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField

data class WeeklyTeacherBean(
    @JSONField(name = "attend")
    var attend: WeeklyTeacherAttendance,
    @JSONField(name = "expend")
    var expend: WeeklyTeacherExpend,
    @JSONField(name = "rank")
    var rank: String,
    @JSONField(name = "summary")
    var summary: WeeklyTeacherSummary,
    @JSONField(name = "work")
    var work: WeeklyTeacherWork
)

data class WeeklyTeacherAttendance(
    @JSONField(name = "attend")
    var attend: String,
    @JSONField(name = "classAttend")
    var classAttend: List<WeeklyTeacherClassAttendance>,
    @JSONField(name = "course")
    var course: String,
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<SchoolHomeAttend>
)

class WeeklyTeacherExpend

data class WeeklyTeacherSummary(
    @JSONField(name = "attend")
    var attend: String,
    @JSONField(name = "expend")
    var expend: String,
    @JSONField(name = "work")
    var work: String
)

class WeeklyTeacherWork

data class WeeklyTeacherClassAttendance(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: String
)