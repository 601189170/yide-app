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
 * @date 2022/4/8 17:54
 * @description 描述
 */
class NotifyStaffViewModel : ViewModel() {


    private val staffListLiveData = MutableLiveData<Result<List<NotifyInfoBean>>>()
    val staffList = staffListLiveData


    /**
     * 请求通知人员- 部门
     * @param id Int
     * @param deptId String
     */
    fun requestStaffList(id: Int, deptId: String){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            map["deptId"] = deptId
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestStaffList(body)
            staffListLiveData.value = result
        }
    }

}