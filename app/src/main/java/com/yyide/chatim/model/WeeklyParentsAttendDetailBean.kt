package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import com.yyide.chatim.widget.treeview.model.NodeId
import java.io.Serializable


data class WeeklyParentsAttendDetailBean(
    @JSONField(name = "attendDetail")
    var attendDetail: List<AttendDetail>,
    @JSONField(name = "attendRate")
    var attendRate: List<AttendRate>,
) : Serializable

data class AttendDetail(
    @JSONField(name = "abNumber")
    var abNumber: Int,
    @JSONField(name = "absence")
    var absence: List<AttendItem>,
    @JSONField(name = "early")
    var early: List<AttendItem>,
    @JSONField(name = "earlyNumber")
    var earlyNumber: Int,
    @JSONField(name = "late")
    var late: List<AttendItem>,
    @JSONField(name = "lateNumber")
    var lateNumber: Int,
    @JSONField(name = "leave")
    var leave: List<AttendItem>,
    @JSONField(name = "leaveNumber")
    var leaveNumber: Int,
    @JSONField(name = "name")
    var name: String
) : Serializable

data class AttendRate(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: String
)

data class AttendItem(
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "value")
    var value: Int,
    @JSONField(name = "list")
    var list: List<AttendItem>
) : NodeId, Serializable {
    override fun getId(): String {
        return ""
    }

    override fun getPId(): String {
        return ""
    }
}

data class AttendanceParentsBean(val name: String, val value: Int, val type: Int)