package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import com.yyide.chatim.widget.treeview.model.NodeId
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
    var value: Double
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
    var absenteeism: ValueChild,
    @JSONField(name = "late")
    var late: ValueChild,
    @JSONField(name = "leave")
    var leave: ValueChild,
    @JSONField(name = "LeaveEarly")
    var LeaveEarly: ValueChild
) : Serializable

data class ValueChild(
    @JSONField(name = "clockDate")
    var clockDate: String,
    @JSONField(name = "detailName")
    var detailName: String,
    @JSONField(name = "statusName")
    var statusName: String,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "type")
    var type: String,
    @JSONField(name = "value")
    var value: List<ValueChild>
) : Serializable, NodeId {
    override fun getId(): String {
        return ""
    }

    override fun getPId(): String {
        return ""
    }
}