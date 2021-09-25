package com.yyide.chatim.kotlin.network

import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.base.BaseNetworkApi

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

}