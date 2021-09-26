package com.yyide.chatim.kotlin.network

import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.base.BaseNetworkApi
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

}