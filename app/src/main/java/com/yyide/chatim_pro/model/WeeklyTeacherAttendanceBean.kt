package com.yyide.chatim_pro.model

import com.alibaba.fastjson.annotation.JSONField
import com.yyide.chatim_pro.widget.treeview.model.NodeId
import java.io.Serializable


data class WeeklyTeacherAttendanceBean(
    @JSONField(name = "teacherAttend")
    var teacherAttend: List<WeeklyTeacherAttend>,
    @JSONField(name = "teacherDetail")
    var teacherDetail: List<WeeklyTeacherDetail>
) : Serializable

data class WeeklyTeacherAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: String
) : Serializable

data class WeeklyTeacherDetail(
    @JSONField(name = "abnormalDetails")
    var abnormalDetails: AbnormalDetail,
    @JSONField(name = "attendName")
    var attendName: String,
    @JSONField(name = "numbers")
    var numbers: StudentNumber
) : Serializable

data class AbnormalDetail(
    @JSONField(name = "absenteeism")
    var absenteeism: List<ValueChild>,
    @JSONField(name = "late")
    var late: List<ValueChild>,
    @JSONField(name = "leave")
    var leave: List<ValueChild>,
    @JSONField(name = "LeaveEarly")
    var LeaveEarly: List<ValueChild>
) : Serializable

data class ValueChild(
    @JSONField(name = "clockName")
    var clockName: String?,
    @JSONField(name = "time")
    var time: String?,
    @JSONField(name = "name")
    var name: String?,
    @JSONField(name = "type")
    var type: String?,
    @JSONField(name = "value")
    var value: List<ValueChild>?
) : Serializable, NodeId {
    override fun getId(): String {
        return ""
    }

    override fun getPId(): String {
        return ""
    }
}