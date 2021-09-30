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
        data class NameBean(
            val id: String? = null,
            val level: Int = 0,
            val type: String? = null,
            val name: String? = null,
            val showName: String? = null,
            val parentId: String? = null,
            val schoolId: String? = null,
            val parentName: String? = null,
        )

      /*  data class DepartmentListBean(
            val id: String? = null,
            val name: String? = null,
        )*/

        data class ParticipantListBean(
            var id: String? = null,
            var name: String? = null,
            var department:Boolean = false,
            var checked:Boolean = false
        )
    }
}