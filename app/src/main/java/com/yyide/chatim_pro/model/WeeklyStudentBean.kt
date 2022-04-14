package com.yyide.chatim_pro.model

import com.alibaba.fastjson.annotation.JSONField
import com.yyide.chatim_pro.widget.treeview.model.NodeId
import java.io.Serializable


data class WeeklyStudentBean(
    @JSONField(name = "studentAttend")
    var studentAttend: List<StudentAttend>,
    @JSONField(name = "studentDetail")
    var studentDetail: List<StudentDetail>
) : Serializable

data class StudentAttend(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: String
) : Serializable

data class StudentDetail(
    @JSONField(name = "abnormalDetails")
    var details: StudentAttendanceBean,
    @JSONField(name = "attendName")
    var attendName: String,
    @JSONField(name = "numbers")
    var numbers: StudentNumber
) : Serializable

data class StudentNumber(
    @JSONField(name = "abNumber")
    var abNumber: Int,
    @JSONField(name = "lateNumber")
    var lateNumber: Int,
    @JSONField(name = "leaveNumber")
    var leaveNumber: Int,
    @JSONField(name = "earlyNumber")
    var earlyNumber: Int
) : Serializable

data class StudentAttendanceBean(
    @JSONField(name = "absenteeism")
    var absenteeism: List<StudentValue>,
    @JSONField(name = "late")
    var late: List<StudentValue>,
    @JSONField(name = "leave")
    var leave: List<StudentValue>,
    @JSONField(name = "LeaveEarly")
    var LeaveEarly: List<StudentValue>
) : Serializable

data class StudentValue(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "type")
    var type: String,
    @JSONField(name = "clockName")
    var clockName: String,
    @JSONField(name = "time")
    var time: String,
    @JSONField(name = "value")
    var value: List<StudentValue>
) : NodeId, Serializable {
    override fun getId(): String {
        return ""
    }

    override fun getPId(): String {
        return ""
    }
}