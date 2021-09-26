package com.yyide.chatim.kotlin.network

import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.model.SchoolWeeklyData
import com.yyide.chatim.model.SchoolWeeklyTeacherBean
import retrofit2.http.GET
import retrofit2.http.Query

interface INetworkService {
    /**
     * 教师班主任周报
     * classId	是	string	班级id
     * teacherId	是	string	老师id
     * startTime	是	string	这周开始时间
     * endTime	是	string	这周结束时间
     */
//    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/face/cloud-face/app/v1/teacher/weekly")
    suspend fun requestTeacherWeekly(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<ResultBean>

    /**
     * 校长周报统计
     * startTime 开始时间
     * endTime   结束时间
     */
    @GET("/face/cloud-face/app/v1/headmaster/weekly")
    suspend fun requestHeadmasterWeekly(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<SchoolWeeklyData>

    /**
     * 家长查看学生周报统计
     * studentId  学生ID
     * startTime  开始时间
     * endTime    结束时间
     */
    @GET("/face/cloud-face/app/v1/student/weekly")
    suspend fun requestStudentWeekly(
        @Query("studentId") studentId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<ResultBean>

    /**
     * 底部文案描述
     */
    @GET("/face/cloud-face/copywriter")
    suspend fun requestCopywriter(): BaseResponse<ResultBean>

    /**
     * 校长查看考勤 教师
     */
    @GET("/face/cloud-face/app/v1/headmaster/teacher/weekly/detail")
    suspend fun requestSchoolTeacherAttendance(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<SchoolWeeklyTeacherBean>


    /**
     * 校长查看考勤 学生
     */
    @GET("/face/cloud-face/app/v1/headmaster/teacher/weekly/detail")
    suspend fun requestSchoolStudentAttendance(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<SchoolWeeklyTeacherBean>
}