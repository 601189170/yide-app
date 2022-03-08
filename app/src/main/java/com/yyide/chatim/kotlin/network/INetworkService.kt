package com.yyide.chatim.kotlin.network

import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.*
import com.yyide.chatim.model.schedule.ScheduleData
import okhttp3.RequestBody
import retrofit2.http.*

interface INetworkService {

    /**
     * 登录
     */
    @POST("/auth/oauth/token")
    suspend fun login(@Body requestBody: RequestBody): BaseResponse<LoginRsp>

    /**
     * 选择身份后登录-获取学校身份信息
     */
    @POST("/cloud/mobile/user/login")
    suspend fun schoolIdentityLogin(@Body requestBody: RequestBody): BaseResponse<UserBean>

    /**
     * 刷新Token
     */
    @POST("/auth/oauth/token")
    suspend fun refreshToken(@Body requestBody: RequestBody): BaseResponse<LoginRsp>

    /**
     * 获取学校信息
     */
    @GET("/cloud/mobile/user/school")
    suspend fun schoolIdentityInfo(): BaseResponse<List<SchoolIdentityRsp>>

    /**
     * 教师班主任周报
     * classId	是	string	班级id
     * teacherId	是	string	老师id
     * startTime	是	string	这周开始时间
     * endTime	是	string	这周结束时间
     */
    @GET("/face/cloud-face/app/v1/teacher/weekly")
    suspend fun requestTeacherWeekly(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyTeacherBean>

    /**
     * 查看教师考勤
     */
    @GET("/face/cloud-face/app/v1/teacher/attend/teacher/detail")
    suspend fun requestAttendanceTeacherDetail(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyTeacherAttendanceBean>

    /**
     * 查看学生考勤
     */
    @GET("/face/cloud-face/app/v1/teacher/attend/student/detail")
    suspend fun requestAttendanceStudentDetail(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyStudentBean>


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
    ): BaseResponse<WeeklyParentsBean>

    /**
     * 家长查看学生周报统计详情
     * studentId  学生ID
     * startTime  开始时间
     * endTime    结束时间
     */
    @GET("/face/cloud-face/app/v1/student/weekly/detail")
    suspend fun requestStudentDetail(
        @Query("studentId") studentId: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<WeeklyParentsAttendDetailBean>

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
    @GET("/face/cloud-face/app/v1/headmaster/student/weekly/detail")
    suspend fun requestSchoolStudentAttendance(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): BaseResponse<SchoolWeeklyTeacherBean>

    /**
     * 会议首页列表
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/management/cloud-system/app/schedule/today/meetingList")
    suspend fun requestMeetingHomeList(@Body requestBody: RequestBody): BaseResponse<List<ScheduleData>>

    /**
     * 会议创建/修改
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/management/cloud-system/app/schedule/saveSchedule")
    suspend fun requestMeetingSave(@Body requestBody: RequestBody): BaseResponse<ResultBean>

    /**
     * 会议列表
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/management/cloud-system/app/schedule/meetingList")
    suspend fun requestMeetingList(@Body requestBody: RequestBody): BaseResponse<List<ScheduleData>>

    /**
     * 查看会议详情
     */
    @POST("/management/cloud-system/app/schedule/findMeetingDetails/{id}")
    suspend fun requestMeetingDetail(
        @Path("id") id: String
    ): BaseResponse<ScheduleData>

    /**
     * 删除会议
     */
    @DELETE("/management/cloud-system/app/schedule/deleteScheduleById/{scheduleId}")
    suspend fun requestMeetingDel(
        @Path("scheduleId") id: String
    ): BaseResponse<ResultBean>
}