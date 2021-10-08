package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable


data class WeeklyTeacherAttendanceBean(
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<WeeklyTeacherAttend>,
    @JSONField(name = "teacherDetail")
    var teacherDetail: List<WeeklyTeacherDetail>
)

data class WeeklyTeacherAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
)

data class WeeklyTeacherDetail(
    @JSONField(name = "abnormalDetails")
    var abnormalDetails: List<AbnormalDetail>,
    @JSONField(name = "attendName")
    var attendName: String,
    @JSONField(name = "numbers")
    var numbers: List<WeeklyTeacherNumber>
) : Serializable

data class AbnormalDetail(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: List<Value>
) : Serializable

data class WeeklyTeacherNumber(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Double
) : Serializable

data class Value(
    @JSONField(name = "clockDate")
    var clockDate: String,
    @JSONField(name = "clockTime")
    var clockTime: String,
    @JSONField(name = "detailName")
    var detailName: String,
    @JSONField(name = "statusName")
    var statusName: String
)