package com.yyide.chatim.model.schedule

/**
 * @author liu tao
 * @date 2021/9/30 14:30
 * @description 描述
 */
data class StudentGuardianRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
//    val data: DataBean? = null,
    val data: DataBean ?=null
) {
    data class DataBean(
        val name: String? = null,
        val badgeUrl: String? = null,
        val id: String? = null,
        val list: List<ListBean>? = null,
//        val participantList:List<ParticipantRsp.DataBean.ParticipantListBean>? = null
    ) {
//        data class ChildListBean(
//            val name: String? = null,
//            val list: List<ListBean>? = null,
//        )
//        {
            data class ListBean(
                val name: String? = null,
                val showName: String? = null,
                val id: String? = null,
                val type: String? = null,
                val participantList:List<ParticipantRsp.DataBean.ParticipantListBean>? = null,
                val guardians:List<ParticipantRsp.DataBean.ParticipantListBean>
            )

    }
}