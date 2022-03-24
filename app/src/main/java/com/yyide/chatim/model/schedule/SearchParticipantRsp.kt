package com.yyide.chatim.model.schedule

/**
 * @author liu tao
 * @date 2021/11/8 11:02
 * @description 描述
 */
data class SearchParticipantRsp(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: DataBean? = null
) {
    data class DataBean(
        var personList: List<TeacherListBean>? = null,
        var list: List<StudentListBean>? = null
    ) {
        data class TeacherListBean(
            val id:String? = null,
            var name: String? = null,
            var userId: String? = null,
            var checked: Boolean = false,
            var departmentName: String? = null,
            var showName: String? = null
        )

        data class StudentListBean(
            var name: String? = null,
            var userId: String? = null,
            var classesName: String? = null,
            var showName: String? = null,
            var guardians: List<GuardiansBean> = mutableListOf()
        ) {
            data class GuardiansBean(
                val id:String? = null,
                var userId: String? = null,
                var guardianName: String? = null,
                var checked: Boolean = false,
                //0.父亲1.母亲2.爷爷3.奶奶4.外公5.外婆，6其他
                var relation:Int = 0
            )
        }
    }
}

fun SearchParticipantRsp.DataBean.TeacherListBean.toParticipantListBean(): ParticipantRsp.DataBean.ParticipantListBean {
    val participantListBean = ParticipantRsp.DataBean.ParticipantListBean()
    participantListBean.name = name
    participantListBean.userId = userId
    participantListBean.realname = name
    participantListBean.department = false
    participantListBean.id = id
    return participantListBean
}

fun SearchParticipantRsp.DataBean.StudentListBean.GuardiansBean.toParticipantListBean():ParticipantRsp.DataBean.ParticipantListBean{
    val participantListBean = ParticipantRsp.DataBean.ParticipantListBean()
    participantListBean.name = guardianName
    participantListBean.userId = userId
    participantListBean.realname = guardianName
    participantListBean.department = false
    participantListBean.id = id
    return participantListBean
}