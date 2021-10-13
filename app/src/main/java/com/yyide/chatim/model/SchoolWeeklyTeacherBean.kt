package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable

data class SchoolWeeklyTeacherBean(
    @JSONField(name = "attend")
    var attend: List<SchoolAttendance>,
    @JSONField(name = "detail")
    var detail: List<Detail>
) : Serializable

data class SchoolAttendance(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
) : Serializable

data class Detail(
    @JSONField(name = "deptAttend")
    var deptAttend: List<DeptAttend>,
    @JSONField(name = "gradeAttend")
    var gradeAttend: List<DeptAttend>,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<TeacherAttendance>,
    @JSONField(name = "studentAttend")
    var studentAttend: List<SchoolAttendance>
) : Serializable

data class DeptAttend(
    @JSONField(name = "lastWeek")
    var lastWeek: Double,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "thisWeek")
    var thisWeek: Double
) : Serializable

data class TeacherAttendance(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "rate")
    var rate: Double
) : Serializable