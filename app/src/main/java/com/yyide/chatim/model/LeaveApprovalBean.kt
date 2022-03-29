package com.yyide.chatim.model

import androidx.room.Ignore
import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable

data class LeaveApprovalBean(
    var code: Int,
    var `data`: DataBean,
    var message: String
) {
    data class DataBean(
        var approver: List<Approver>,
        var ccList: List<Cc>,
        var classList: List<LeaveClassBean>,
        var dept: DeptBean,
        var hours: Float,
        var procId: String,
        var procKey: String,
        var sponsorType: String
    )

    data class DeptBean(
        @JSONField(name = "id")
        var id: String = "",
        @JSONField(name = "name")
        var name: String = "",
        @JSONField(name = "avatar")
        var avatar: String = ""
    )

    data class LeaveCommitBean(
        @JSONField(name = "id")
        var id: String = "",
        @JSONField(name = "name")
        var name: String = "",
        var approverName: String = "",
        @JSONField(name = "userId")
        var userId: String = "",
        var avatar: String = "",
        var branapprList: List<Branappr>? = null,
    ) : Serializable

    data class Approver(
        var apprType: Int,
        var branapprList: List<Branappr>,
        var id: String,
        var name: String,
        var pid: String,
        var sort: Int,
        var status: String,
        var userType: Int
    ) {
        companion object {
            //审批人类型1：校长 、 2：校区负责任人 、3：学段负责人 、4：年级负责人、 5：班主任、 6：家长 、7：部门负责任人 、99：指定成员】
            const val DESIGNATED_MEMBER = 99
        }
    }

    data class Cc(
        @JSONField(name = "ccId")
        var ccId: Long = 0,
        @JSONField(name = "id")
        var id: Long = 0,
        @JSONField(name = "name")
        var name: String = "",
        @JSONField(name = "procApprId")
        var procApprId: String = "",
        @JSONField(name = "avatar")
        var avatar: String = ""
    ) : Serializable

    data class Branappr(
        var id: String = "",
        var name: String = "",
        var avatar: String = ""
    )

    data class RequestParam(
        var id: String = "",
        var name: String = "",
        var userId: String = ""
    )

    data class LeaveRequestBean(
        @JSONField(name = "startTime")
        var startTime: String = "",
        @JSONField(name = "endTime")
        var endTime: String = "",
        @JSONField(name = "hours")
        var hours: Float = 0f,
        @JSONField(name = "reason")
        var reason: String = "",
        @JSONField(name = "student")
        var student: String = "",
        @JSONField(name = "studentId")
        var studentId: String = "",
    )

    data class LeaveClassBean(
        @JSONField(name = "id")
        var id: String = "",
        @JSONField(name = "name")
        var name: String = "",
        @JSONField(name = "studentId")
        var studentId: String = "",
        @JSONField(name = "studentName")
        var studentName: String = ""
    )

}


