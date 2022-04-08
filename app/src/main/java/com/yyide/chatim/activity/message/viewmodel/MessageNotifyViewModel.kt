package com.yyide.chatim.activity.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.message.MessagePushNetWork
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 10:00
 * @description 描述
 */
class MessageNotifyViewModel : ViewModel() {

    /**
     * 查询教职工通知列表
     */
    fun requestStaffInfoList(id: Int){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            map["identity"] = 1
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestReNotifyInfo(body)
        }
    }

    /**
     * 查询家长通知列表
     */
    fun requestNotifyInfo(id: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            map["identity"] = 2
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestReNotifyInfo(body)
        }
    }

}