package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField

data class SchoolWeeklyTeacherBean(
    @JSONField(name = "attend")
    var attend: List<SchoolAttendance>,
    @JSONField(name = "detail")
    var detail: List<SchoolWeeklyDetail>
)

data class SchoolAttendance(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Int
)

data class SchoolWeeklyDetail(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "deptAttend")
    var deptAttend: List<DeptAttendance>,
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<TeacherAttendance>
)

data class DeptAttendance(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "lastWeek")
    var lastWeek: Int,
    @JSONField(name = "thisWeek")
    var thisWeek: Int
)

data class TeacherAttendance(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
)