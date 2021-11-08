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
        var teacherList: List<TeacherListBean>? = null,
        var studentList: List<StudentListBean>? = null
    ) {
        data class TeacherListBean(
            val id:String? = null,
            var name: String? = null,
            var userId: String? = null,
            var checked: Boolean = false,
            var departmentName: String? = null
        )

        data class StudentListBean(
            var name: String? = null,
            var userId: String? = null,
            var classesName: String? = null,
            var guardians: List<GuardiansBean> = mutableListOf()
        ) {
            data class GuardiansBean(
                val id:String? = null,
                var userId: String? = null,
                var guardianName: String? = null,
                var checked: Boolean = false
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