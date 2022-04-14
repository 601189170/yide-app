package com.yyide.chatim_pro.kotlin.network.message

import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.base.BaseNetworkApi
import com.yyide.chatim_pro.model.message.AcceptMessageBean
import okhttp3.RequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 19:50
 * @description 信息api
 */
object MessagePushNetWork :
    BaseNetworkApi<IMessagePushService>(BaseConstant.API_SERVER_URL) {

    /**
     * 收到消息列表
     * @param requestBody RequestBody
     * @return Result<AcceptMessageBean>
     */
    suspend fun requestAcceptMessageList(requestBody: RequestBody): Result<AcceptMessageBean> {
        return getResult {
            service.requestAcceptMessage(requestBody)
        }
    }

    /**
     * 收到消息item内容
     * @param requestBody RequestBody
     */
    suspend fun requestAcceptMessageInfo(requestBody: RequestBody) =
        getResult {
            service.requestAcceptItemDetail(requestBody)
        }


    /**
     * 收到消息 确认item内容
     * @param requestBody RequestBody
     */
    suspend fun confirmAcceptMessageInfo(requestBody: RequestBody) =
        getResult {
            service.confirmAcceptItemDetail(requestBody)
        }


    /**
     * 发布消息列表
     * @param requestBody RequestBody
     * @return Result<AcceptMessageBean>
     */
    suspend fun requestPublishMessageList(requestBody: RequestBody): Result<AcceptMessageBean> {
        return getResult {
            service.requestPublishMessage(requestBody)
        }
    }

    /**
     * 发布消息item内容
     * @param requestBody RequestBody
     */
    suspend fun requestPublishMessageInfo(requestBody: RequestBody) =
        getResult {
            service.requestPublishItemDetail(requestBody)
        }

    /**
     * 发布消息 撤销item消息
     * @param requestBody RequestBody
     */
    suspend fun requestRevokeMessage(requestBody: RequestBody) =
        getResult {
            service.requestRevokeMessage(requestBody)
        }

    /**
     * 发布消息 置顶/取消消息
     * @param requestBody RequestBody
     */
    suspend fun requestReTopMessage(requestBody: RequestBody) =
        getResult {
            service.requestReTopMessage(requestBody)
        }

    /**
     * 发布消息 请求部门/班级列表
     * @param requestBody RequestBody
     */
    suspend fun requestReNotifyInfo(requestBody: RequestBody) =
        getResult {
            service.requestNotifyInfo(requestBody)
        }


    /**
     * 发布消息 请求部门人员
     * @param requestBody RequestBody
     */
    suspend fun requestStaffList(requestBody: RequestBody) =
        getResult {
            service.requestNotifyStaffList(requestBody)
        }

    /**
     * 发布消息 请求家长人员
     * @param requestBody RequestBody
     */
    suspend fun requestParentList(requestBody: RequestBody) =
        getResult {
            service.requestNotifyParentList(requestBody)
        }


    /**
     * 首页确认弹窗
     */
    suspend fun showHomeDialog() =
        getResult {
            service.requestHomeDialog()
        }

}