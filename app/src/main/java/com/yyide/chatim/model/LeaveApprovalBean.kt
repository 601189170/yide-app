package com.yyide.chatim.model

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
        var dept: String,
        var hours: String,
        var procId: String,
        var procKey: String,
        var sponsorType: String
    )

    data class LeaveCommitBean(val id: String = "", val name: String = "", val userId: String = "")

    data class Approver(
        var apprType: Int,
        var branapprList: List<Branappr>,
        var id: String,
        var name: String,
        var pid: String,
        var sort: Int,
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
        var procApprId: String = ""
    ) : Serializable

    data class Branappr(
        var approverId: String,
        var name: String
    )

    data class LeaveRequestBean(
        @JSONField(name = "startTime")
        var startTime: String = "",
        @JSONField(name = "endTime")
        var endTime: String = "",
        @JSONField(name = "hours")
        var hours: String = "",
        @JSONField(name = "reason")
        var reason: String = "",
        @JSONField(name = "student")
        var student: String = ""
    )

}


