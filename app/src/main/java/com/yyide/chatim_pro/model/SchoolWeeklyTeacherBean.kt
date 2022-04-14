package com.yyide.chatim_pro.model

import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable

data class SchoolWeeklyTeacherBean(
    @JSONField(name = "attend")
    var attend: List<SchoolHomeAttend>,
    @JSONField(name = "detail")
    var detail: List<Detail>
) : Serializable

data class Detail(
    @JSONField(name = "deptAttend")
    var deptAttend: List<DeptAttend>,
    @JSONField(name = "gradeAttend")
    var gradeAttend: List<DeptAttend>,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "teacherAttend")
    var teacherAttend: ArrayList<SchoolHomeAttend>,
    @JSONField(name = "studentAttend")
    var studentAttend: ArrayList<SchoolHomeAttend>
) : Serializable

data class DeptAttend(
    @JSONField(name = "lastWeek")
    var lastWeek: Double,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "thisWeek")
    var thisWeek: Double
) : Serializable
