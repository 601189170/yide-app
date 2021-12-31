package com.yyide.chatim.net

import com.yyide.chatim.model.gate.ClassListOfTeacherBean
import com.yyide.chatim.model.gate.GateBaseRsp
import com.yyide.chatim.model.gate.SiteBean
import retrofit2.http.GET

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
}