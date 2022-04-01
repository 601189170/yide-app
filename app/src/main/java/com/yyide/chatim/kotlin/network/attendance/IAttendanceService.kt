package com.yyide.chatim.kotlin.network.attendance

import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.attendance.teacher.*
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
    @GET("/school/schoolAttendanceClock/getClockRecordByMonth")
    suspend fun requestClockRecordByMonth(@Path("month") day: String): BaseResponse<TeacherStatisticsMonthBean>

    /**
     * 查询教师考勤的日度考勤
     */
    @GET("/school/schoolAttendanceClock/getClockRecordByDay/{day}")
    suspend fun requestClockRecordByDay(@Path("day") day: String): BaseResponse<TeacherStatisticsDayBean>


    /**
     * 查询教师考勤考勤规则
     */
    @GET("/school/schoolAttendanceClock/getClockRule")
    suspend fun requestAttendanceRule(): BaseResponse<TeacherAttendanceRuleBean>


    /**
     * 查询教师考勤信息
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/school/schoolAttendanceClock/getClockMessage")
    suspend fun requestPunchMessage(@Body requestBody: RequestBody): BaseResponse<PunchMessageBean>


    /**
     * 地址打卡
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/school/schoolAttendanceClock/clockByAddress")
    suspend fun punchByAddress(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * 地址打卡
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/school/schoolAttendanceClock/clockByWiFi")
    suspend fun punchByWifi(@Body requestBody: RequestBody): BaseResponse<String>
}