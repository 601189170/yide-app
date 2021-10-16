package com.yyide.chatim.model.schedule

/**
 * @author liu tao
 * @date 2021/9/28 13:35
 * @description 描述
 */
data class ParticipantRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
    val data: DataBean? = null,
) {
    data class DataBean(
        var name: String? = null,
        var departmentList: List<ParticipantListBean>? = null,
        var participantList: List<ParticipantListBean>? = null
    ) {
        data class ParticipantListBean(
            var id: String? = null,
            var userId:String?=null,
            var teacherId:String?=null,
            var participantId:String?=null,
            var userName:String?=null,
            var name: String? = null,
            var realname: String? = null,
            var type: String? = null,
            var department:Boolean = false,
            var checked:Boolean = false
        )
    }
}