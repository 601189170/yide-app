package com.yyide.chatim_pro.activity.message.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.message.MessagePushNetWork
import com.yyide.chatim_pro.model.message.AcceptItemInfo
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/7 11:21
 * @description 描述
 */
class NoticeContentViewModel : ViewModel() {


    private val messageInfoLiveData = MutableLiveData<Result<AcceptItemInfo>>()
    val messageInfo = messageInfoLiveData

    private val confirmLiveData = MutableLiveData<Result<Boolean>>()
    val confirmInfo = confirmLiveData

    fun getDetail(rId: Int, cType: Int, mType: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["receiveId"] = rId
            map["contentType"] = cType
            map["messType"] = mType
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestAcceptMessageInfo(body)
            messageInfoLiveData.value = result
        }
    }

    fun confirmDetail(rId: Int){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["receiveId"] = rId
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.confirmAcceptMessageInfo(body)
            confirmLiveData.value = result
        }
    }

}