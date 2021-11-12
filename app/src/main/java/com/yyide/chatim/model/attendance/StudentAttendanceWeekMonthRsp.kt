package com.yyide.chatim.model.attendance

/**
 * @author liu tao
 * @date 2021/11/3 10:28
 * @description 描述
 */
data class StudentAttendanceWeekMonthRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
    val data: DataBean? = null
) {
    data class DataBean(
        val className: String? = null,
        val weeksMonthStatisticalForm: WeeksMonthStatisticalFormBean? = null,
        val classroomTeacherAttendanceList: List<ClassroomTeacherAttendanceListBean>? = null
    ) {
        data class WeeksMonthStatisticalFormBean(
            val weeksMonthStatisticalCourseForm: WeeksMonthListBean? = null,
            val weeksMonthList: List<WeeksMonthListBean>? = null
        ) {
            data class WeeksMonthListBean(
                val absenteeism: Int = 0,
                val late: Int = 0,
                val leave: Int = 0,
                val early: Int = 0,
                val normal: Int = 0,
                val signInOutRate: String? = null,
                //事件类型id
                val serverId: String? = null,
                val theme: String? = null,
                val type: Int = 0,
                val absenteeismList: List<EventFormBean>? = null,
                val lateList: List<EventFormBean>? = null,
                val leaveList: List<EventFormBean>? = null,
                val normalList: List<EventFormBean>? = null,
                val earlyList: List<EventFormBean>? = null,
                val courseNumber: Int = 0,

            ) {
                data class EventFormBean(
                    val attendanceType: String? = null,
                    val requiredTime: String? = null,
                    val sortName: String? = null,
                    val eventName: String? = null,
                    val signInTime: String? = null,
                    val clockName: String? = null,
                    val section: String? = null,
                    val timeFrame: String? = null,
                    val courseInfo: String? = null,
                    val courseName: String? = null,
                    val courseStartTime: String? = null,
                    val sectionOrder: String? = null,
                    var leaveStartTime: String? = null,
                    var leaveEndTime: String? = null,
                    var attendanceSignInOut: String? = null
                )
            }
        }

        data class ClassroomTeacherAttendanceListBean(
            val theme: String? = null,
            val serverId: String? = null,
            val type: Int = 0,
        )
    }
}