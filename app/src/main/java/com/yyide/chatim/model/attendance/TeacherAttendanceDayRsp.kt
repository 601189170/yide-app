package com.yyide.chatim.model.attendance

/**
 * @author liu tao
 * @date 2021/11/3 11:48
 * @description 描述
 */
data class TeacherAttendanceDayRsp(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: DataBean? = null
) {
    data class DataBean(
        var courseBasicVoForm: List<EventBasicVoListBean>? = null,
        var eventBasicVoList: List<EventBasicVoListBean>? = null,
        var classroomTeacherAttendanceList: List<ClassroomTeacherAttendanceListBean>? = null,
    ) {
        data class ClassroomTeacherAttendanceListBean(
            val theme: String? = null,
            val serverId: String? = null,
            val type: Int = 0,
        )
        data class EventBasicVoListBean(
            var theme: String? = null,
            var eventName: String? = null,
            var courseTime:String? = null,
            var attendanceSignInOut: String? = null,
            var signInOutRate: String? = null,
            var serverId: String? = null,
            var attendanceTimeId: String? = null,
            var peopleType: String? = null,
            var type: Int = 0,
            var normal: Int = 0,
            var late: Int = 0,
            var leave: Int = 0,
            var absenteeism: Int = 0,
            var totalNumber: Int = 0,
            var requiredTime: String? = null,
            var allRollList: List<EventBean>? = null,
            var absenteeismList: List<EventBean>? = null,
            var leaveList: List<EventBean>? = null,
            var lateList: List<EventBean>? = null,
            var normalList: List<EventBean>? = null
        ) {
            data class EventBean(
                var userName: String? = null,
                var userId: String? = null,
                var attendanceType: String? = null,
                var requiredTime: String? = null,
                var clockName:String? = null,
                var signInTime:String? = null,
                var leaveStartTime: String? = null,
                var leaveEndTime: String? = null,
                var attendanceSignInOut: String? = null
            )
        }
    }
}