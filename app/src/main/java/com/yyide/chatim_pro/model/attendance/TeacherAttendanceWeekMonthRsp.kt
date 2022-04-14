package com.yyide.chatim_pro.model.attendance

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *
 * @author liu tao
 * @date 2021/11/3 13:52
 * @description 描述
 */
data class TeacherAttendanceWeekMonthRsp(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: DataBean? = null
) {
    data class DataBean(
        var weeksCourseForm: WeeksEvenListBean? = null,
        var weeksEvenList: List<WeeksEvenListBean>? = null,
        var classroomTeacherAttendanceList: List<ClassroomTeacherAttendanceListBean>? = null,
    ) {
        data class ClassroomTeacherAttendanceListBean(
            val theme: String? = null,
            val serverId: String? = null,
            val type: Int = 0,
        )

        data class WeeksEvenListBean(
            var attendanceAppNumberVoForm: AttendanceAppNumberVoFormBean? = null,
            var serverId: String? = null,
            val theme: String? = null,
            val type: Int = 0,
            var absenteeismList: List<EventBean>? = null,
            var leaveList: List<EventBean>? = null,
            var lateList: List<EventBean>? = null,
            var earlyList: List<EventBean>? = null,

        ) {
            data class AttendanceAppNumberVoFormBean(
                var late: Int = 0,
                var early: Int = 0,
                var leave: Int = 0,
                var absenteeism: Int = 0,
                var courseNumber: Int = 0
            )
            data class EventBean(
                var userName: String? = null,
                var userId: String? = null,
                var sectionNumber: Int = 0,
                var checked:Boolean = false,
                var courseInfoFormList: List<CourseInfoFormListBean>? = null
            ) {
                data class CourseInfoFormListBean(
                    var attendanceType: String? = null,
                    var requiredTime: String? = null,
                    var signInTime: String? = null,
                    var clockName: String? = null,
                    var sortName: String? = null,
                    var eventName: String? = null,
                    var section: String? = null,
                    var timeFrame: String? = null,
                    var courseInfo: String? = null,
                    var courseName: String? = null,
                    var courseStartTime: String? = null,
                    var sectionOrder: String? = null,
                    var leaveStartTime: String? = null,
                    var leaveEndTime: String? = null,
                    var attendanceSignInOut: String? = null
                )
            }
        }
    }
}

data class EventBean(
    var userName: String? = null,
    var userId: String? = null,
    var sectionNumber: Int = 0,
    var type: Int = 0,//0第一层 1第二层
    var checked: Boolean = false,
    var attendanceType: String? = null,
    var requiredTime: String? = null,
    var signInTime: String? = null,
    var clockName: String? = null,
    var sortName: String? = null,
    var eventName: String? = null,
    var section: String? = null,
    var timeFrame: String? = null,
    var courseInfo: String? = null,
    var courseName: String? = null,
    var courseStartTime: String? = null,
    var sectionOrder: String? = null,
    var leaveStartTime: String? = null,
    var leaveEndTime: String? = null,
    var attendanceSignInOut: String? = null
) : MultiItemEntity {
    override val itemType: Int
        get() = if (type == 0) GROUP_ITEM else CHILD_ITEM

    companion object {
        const val GROUP_ITEM = 1
        const val CHILD_ITEM = 2
    }
}

