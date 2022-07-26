package com.yyide.chatim_pro.kotlin.network.attendance

import com.yyide.chatim_pro.kotlin.network.base.BaseResponse
import com.yyide.chatim_pro.model.attendance.teacher.*
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *
 * @author shenzhibin
 * @date 2022/3/30 17:57
 * @description 教师考勤的api接口
 */
interface IAttendanceService {
    /**
     * 查询教师考勤的月度考勤
     */
    @GET("/attweb/attendanceWorkClock/getClockRecordByMonth")
    suspend fun requestClockRecordByMonth(@Query("month") day: String): BaseResponse<TeacherStatisticsMonthBean>

    /**
     * 查询教师考勤的日度考勤
     */
    @GET("/attweb/attendanceWorkClock/getClockRecordByDay")
    suspend fun requestClockRecordByDay(@Query("day") day: String): BaseResponse<DailyRecordItem>


    /**
     * 查询教师考勤考勤规则
     */
    @GET("/attweb/attendanceWorkClock/getClockRule")
    suspend fun requestAttendanceRule(): BaseResponse<TeacherAttendanceRuleBean>


    /**
     * 查询教师考勤信息
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/attweb/attendanceWorkClock/getClockMessage")
    suspend fun requestPunchMessage(@Body requestBody: RequestBody): BaseResponse<PunchMessageBean>


    /**
     * 地址打卡
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/attweb/attendanceWorkClock/clockByAddress")
    suspend fun punchByAddress(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * 外勤打卡
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/attweb/attendanceWorkClock/clockByOutside")
    suspend fun punchByOutSide(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * wifi打卡
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/attweb/attendanceWorkClock/clockByWiFi")
    suspend fun punchByWifi(@Body requestBody: RequestBody): BaseResponse<String>
}