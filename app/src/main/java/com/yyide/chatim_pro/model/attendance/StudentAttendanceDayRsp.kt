package com.yyide.chatim_pro.model.attendance

import com.google.gson.annotations.SerializedName

/**
 * @author liu tao
 * @date 2021/11/3 10:16
 * @description 描述
 */
data class StudentAttendanceDayRsp(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: DataBean? = null
) {
    data class DataBean(
        var className: String? = null,
        var studentId: String? = null,
        var appStudentDailyStatisticalForm: AppStudentDailyStatisticalFormBean? = null,
        var classroomTeacherAttendanceList: List<ClassroomTeacherAttendanceListBean>? = null,
        var studentInfos: List<StudentBean>?=null
    ) {
        data class AppStudentDailyStatisticalFormBean(
            var eventList: List<EventFormListBean>? = null,
            var courseFormList: List<EventFormListBean>? = null
        ) {
            data class EventFormListBean(
                var theme: String? = null,
                var eventName: String? = null,
                var type: Int = 0,
                var sortName: String? = null,
                var attendanceType: String? = null,
                var attendanceSignOut: String? = null,
                var requiredTime: String? = null,
                @SerializedName("url")
                var path: String? = null,
                var signTime: String? = null,
                var serverId: String? = null,
                var courseName: String? = null,
                var timeFrame: String? = null,
                var section: String? = null,
                var courseInfo: String? = null,
                var sectionOrder: String? = null,
                var leaveStartTime: String? = null,
                var leaveEndTime: String? = null
            )
        }

        data class ClassroomTeacherAttendanceListBean(
            var theme: String? = null,
            var eventName: String? = null,
            var serverId: String? = null,
            var type: Int = 0
        )

        data class StudentBean(
            var studentId: String? = null,
            var classId: String? = null,
            var studentName: String? = null,
            var className: String? = null
        )
    }
}