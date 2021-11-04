package com.yyide.chatim.kotlin.network

import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.base.BaseNetworkApi
import okhttp3.RequestBody
import retrofit2.http.Query

/**
 * 网络请求具体实现
 */
object NetworkApi : BaseNetworkApi<INetworkService>(BaseConstant.API_SERVER_URL) {

    /**
     * 教师/班主任周报
     */
    suspend fun teacherWeekly(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) = getResult {
        service.requestTeacherWeekly(classId, teacherId, startTime, endTime)
    }

    /**
     * 校长周报
     */
    suspend fun schoolWeekly(startTime: String, endTime: String) = getResult {
        service.requestHeadmasterWeekly(startTime, endTime)
    }

    /**
     * 家长周报
     */
    suspend fun parentsWeekly(studentId: String, startTime: String, endTime: String) = getResult {
        service.requestStudentWeekly(studentId, startTime, endTime)
    }

    /**
     * 家长周报
     */
    suspend fun requestCopywriter() = getResult {
        service.requestCopywriter()
    }

    /**
     * 校长查看全校教师考勤
     */
    suspend fun requestSchoolTeacherAttendance(startTime: String, endTime: String) = getResult {
        service.requestSchoolTeacherAttendance(startTime, endTime)
    }

    /**
     * 校长查看全校学生考勤
     */
    suspend fun requestSchoolStudentAttendance(startTime: String, endTime: String) = getResult {
        service.requestSchoolStudentAttendance(startTime, endTime)
    }

    /**
     * 班主任查看老师考勤
     */
    suspend fun requestAttendanceTeacherDetail(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) = getResult {
        service.requestAttendanceTeacherDetail(classId, teacherId, startTime, endTime)
    }

    /**
     * 老师查看学生考勤
     */
    suspend fun requestAttendanceStudentDetail(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) = getResult {
        service.requestAttendanceStudentDetail(classId, teacherId, startTime, endTime)
    }

    /**
     * 家长周报
     */
    suspend fun requestParentsStudentDetail(studentId: String, startTime: String, endTime: String) =
        getResult {
            service.requestStudentDetail(studentId, startTime, endTime)
        }

    /**
     * 会议首页今日列表
     */
    suspend fun requestMeetingHomeList(requestBody: RequestBody) =
        getResult {
            service.requestMeetingHomeList(requestBody)
        }

    /**
     * 会议创建、修改
     */
    suspend fun requestMeetingSave(requestBody: RequestBody) =
        getResult {
            service.requestMeetingSave(requestBody)
        }

    /**
     * 会议列表
     */
    suspend fun requestMeetingList(requestBody: RequestBody) =
        getResult {
            service.requestMeetingList(requestBody)
        }

    /**
     * 会议详情
     */
    suspend fun requestMeetingDetail(id: String) =
        getResult {
            service.requestMeetingDetail(id)
        }

    /**
     * 会议详情
     */
    suspend fun requestMeetingDel(scheduleId: String) =
        getResult {
            service.requestMeetingDel(scheduleId)
        }
}