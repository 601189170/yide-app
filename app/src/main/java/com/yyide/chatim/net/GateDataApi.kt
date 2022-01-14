package com.yyide.chatim.net

import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.PushSettingBean
import com.yyide.chatim.model.gate.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 * @author liu tao
 * @date 2021/12/30 17:15
 * @description 闸机数据api
 */
interface GateDataApi {
    /**
     * 获取当前或传入的teacherid的班级
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v2/queryClassInfoByTeacherId
     */
    @GET("/management/cloud-system/barrier-server/barrier/mobile/v2/queryClassInfoByTeacherId")
    suspend fun queryClassInfoByTeacherId(): GateBaseRsp<List<ClassListOfTeacherBean>>

    /**
     * 闸机建筑场地列表
     * https://api.uat.edu.1d1j.net/management/cloud-system/v1/web/barrier/site/list
     */
    @GET("/management/cloud-system/v1/web/barrier/site/list")
    suspend fun siteList(): GateBaseRsp<List<SiteBean>>

    /**
     * 获取首页学生基础信息
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v3/index/queryBasicsInfoBySiteId
     */
    @POST("/management/cloud-system/barrier-server/barrier/mobile/v3/index/queryBasicsInfoBySiteId")
    suspend fun queryBasicsInfoBySiteId(@Body requestBody: RequestBody): GateBaseRsp<GateBaseInfoBean>

    /**
     * 获取首页学段统计信息
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v3/index/queryBarrierSectionStatistical
     */
    @POST("/management/cloud-system/barrier-server/barrier/mobile/v3/index/queryBarrierSectionStatistical")
    suspend fun queryBarrierSectionStatistical(@Body requestBody: RequestBody): GateBaseRsp<List<BarrierSectionBean>>


    /**
     * 全部、学段、年级、班级详情
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v2/queryAllStudentPassageInOutDetails
     */
    @POST("/management/cloud-system/barrier-server/barrier/mobile/v2/queryAllStudentPassageInOutDetails")
    suspend fun queryAllStudentPassageInOutDetails(@Body requestBody: RequestBody): GateBaseRsp<GateThroughPeopleListBean>

    /**
     * 个人详情
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v2/queryPersonalPassageDetails
     */
    @POST("/management/cloud-system/barrier-server/barrier/mobile/v2/queryPersonalPassageDetails")
    suspend fun queryPersonalPassageDetails(@Body requestBody: RequestBody): GateBaseRsp<ThroughPeopleDetail>

    /**
     * 全校通行数据/学段/年级
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v2/queryAllStudentInOutInfo
     */
    @POST("/management/cloud-system/barrier-server/barrier/mobile/v2/queryAllStudentInOutInfo")
    suspend fun queryAllStudentInOutInfo(@Body requestBody: RequestBody): GateBaseRsp<StudentGradeInOutInfoBean>

    /**
     * 获取当前或传入的家长学生
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v2/queryStudentInfoByUserId
     */
    @GET("/management/cloud-system/barrier-server/barrier/mobile/v2/queryStudentInfoByUserId")
    suspend fun queryStudentInfoByUserId(): GateBaseRsp<List<StudentInfoBean>>

    /**
     * 闸机统计教职工出入数--部门
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v2/queryTeacherBarrierPassageDataByDeptId
     */
    @POST("/management/cloud-system/barrier-server/barrier/mobile/v2/queryTeacherBarrierPassageDataByDeptId")
    suspend fun queryTeacherBarrierPassageDataByDeptId(@Body requestBody: RequestBody): GateBaseRsp<StaffBean>

    /**
     * 获取教职工全部详情
     * https://api.uat.edu.1d1j.net/management/cloud-system/barrier-server/barrier/mobile/v2/queryAllTeacherPassageInOutDetails
     */
    @POST("/management/cloud-system/barrier-server/barrier/mobile/v2/queryAllTeacherPassageInOutDetails")
    suspend fun queryAllTeacherPassageInOutDetails(@Body requestBody: RequestBody): GateBaseRsp<GateThroughPeopleListBean>

    /**
     * 查询用户通知开关
     * https://api.uat.edu.1d1j.net/message/cloud-message/message-push/push/mobile/v1/queryUserNoticeOnOffByUserId
     */
    @GET("/message/cloud-message/message-push/push/mobile/v1/queryUserNoticeOnOffByUserId")
    suspend fun queryUserNoticeOnOffByUserId(): GateBaseRsp<List<PushSettingBean>>

    /**
     * 更新用户开关
     * https://api.uat.edu.1d1j.net/message/cloud-message/message-push/push/mobile/v1/updateUserNoticeOnOffByUserIdAndType
     */
    @POST("/message/cloud-message/message-push/push/mobile/v1/updateUserNoticeOnOffByUserIdAndType")
    suspend fun updateUserNoticeOnOffByUserIdAndType(@Body requestBody: RequestBody): BaseRsp
}