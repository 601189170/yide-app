package com.yyide.chatim.kotlin.network.message

import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.message.AcceptItemInfo
import com.yyide.chatim.model.message.AcceptMessageBean
import com.yyide.chatim.model.message.NotifyInfoBean
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 19:50
 * @description 信息发布的api接口
 */
interface IMessagePushService {

    /**
     * 信息发布-收到消息列表
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/receive/page")
    suspend fun requestAcceptMessage(@Body requestBody: RequestBody): BaseResponse<AcceptMessageBean>

    /**
     * 信息发布-收到消息详情
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/receive/getInfo")
    suspend fun requestAcceptItemDetail(@Body requestBody: RequestBody): BaseResponse<AcceptItemInfo>

    /**
     * 信息发布-收到消息详情-确认
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/receive/updateIncrementconfirms")
    suspend fun confirmAcceptItemDetail(@Body requestBody: RequestBody): BaseResponse<Boolean>


    /**
     * 信息发布-发布消息列表
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/publish/page")
    suspend fun requestPublishMessage(@Body requestBody: RequestBody): BaseResponse<AcceptMessageBean>

    /**
     * 信息发布-发布消息详情
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/publish/getInfo")
    suspend fun requestPublishItemDetail(@Body requestBody: RequestBody): BaseResponse<AcceptItemInfo>

    /**
     * 信息发布-发布消息详情-撤回
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/publish/updateRetract")
    suspend fun requestRevokeMessage(@Body requestBody: RequestBody): BaseResponse<Boolean>

    /**
     * 信息发布-发布消息详情-置顶
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/publish/updateTop")
    suspend fun requestReTopMessage(@Body requestBody: RequestBody): BaseResponse<Boolean>

    /**
     * 信息发布-发布消息详情-通知列表
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/cloud/mobile/mess/publish/notifyInfo")
    suspend fun requestNotifyInfo(@Body requestBody: RequestBody): BaseResponse<List<NotifyInfoBean>>
}