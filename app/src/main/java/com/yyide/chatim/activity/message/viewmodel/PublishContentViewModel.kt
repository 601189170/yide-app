package com.yyide.chatim.activity.message.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.message.MessagePushNetWork
import com.yyide.chatim.model.message.AcceptItemInfo
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/7 11:21
 * @description 描述
 */
class PublishContentViewModel : ViewModel() {


    private val messageInfoLiveData = MutableLiveData<Result<AcceptItemInfo>>()
    val messageInfo = messageInfoLiveData

    private val revokeLiveData = MutableLiveData<Result<Boolean>>()
    val revokeResult = revokeLiveData

    private val reTopLiveData = MutableLiveData<Result<Boolean>>()
    val reTopResult = reTopLiveData


    fun getDetail(pId: Int, cType: Int, mType: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = pId
            map["contentType"] = cType
            map["messType"] = mType
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestPublishMessageInfo(body)
            messageInfoLiveData.value = result
        }
    }

    /**
     * 撤回发布消息
     * @param id Int
     */
    fun revokePublishMessage(id: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestRevokeMessage(body)
            revokeResult.value = result
        }
    }

    /**
     * 置顶/取消 发布消息
     * @param id Int
     */
    fun reTopPublishMessage(id: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestReTopMessage(body)
            reTopResult.value = result
        }
    }

}