package com.yyide.chatim_pro.model

import com.alibaba.fastjson.annotation.JSONField


data class WeeklySchoolTeacherBean(
    @JSONField(name = "attend")
    var attend: List<WeeklySchoolTeacherAttend>,
    @JSONField(name = "detail")
    var detail: List<WeeklySchoolTeacherDetail>
)

data class WeeklySchoolTeacherAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Int
)

data class WeeklySchoolTeacherDetail(
    @JSONField(name = "deptAttend")
    var deptAttend: List<WeeklySchoolTeacherDeptAttend>,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<WeeklySchoolTeacherTeacherAttend>
)

data class WeeklySchoolTeacherDeptAttend(
    @JSONField(name = "lastWeek")
    var lastWeek: Int,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "thisWeek")
    var thisWeek: Int
)

data class WeeklySchoolTeacherTeacherAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "rate")
    var rate: Double
)