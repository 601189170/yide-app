package com.yyide.chatim_pro.activity.message.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.message.MessagePushNetWork
import com.yyide.chatim_pro.model.message.NotifyInfoBean
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 10:00
 * @description 描述
 */
class MessageNotifyViewModel : ViewModel() {


    private val staffListLiveData = MutableLiveData<Result<List<NotifyInfoBean>>>()
    val staffList = staffListLiveData

    private val parentListLiveData = MutableLiveData<Result<List<NotifyInfoBean>>>()
    val parentList = parentListLiveData


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
            staffList.value = result
        }
    }

    /**
     * 查询家长通知列表
     */
    fun requestParentInfoList(id: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            map["identity"] = 2
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestReNotifyInfo(body)
            parentList.value = result
        }
    }

}