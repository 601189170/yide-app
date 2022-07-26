package com.yyide.chatim_pro.model

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
        var ccType: String,//抄送人类型1：校长 、 2：校区负责任人 、3：学段负责人 、4：年级负责人、 5：班主任、 6：家长 、7：部门负责任人 、98：发起人自选、99：指定成员】
        var classList: List<LeaveClassBean>,
        var dept: DeptBean,
        var hours: Float,
        var procId: String,
        var procKey: String,
        var isBack: Boolean = false,
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
        var id: String = "",
        var name: String = "",
        var approverName: String = "",
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
        var isBack: Boolean,
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
        var isCheck: Boolean = false,
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


