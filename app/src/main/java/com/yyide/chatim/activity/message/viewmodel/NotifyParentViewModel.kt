package com.yyide.chatim.activity.message.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.message.MessagePushNetWork
import com.yyide.chatim.model.message.NotifyParentBean
import com.yyide.chatim.model.message.ResponseParentBean
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 17:55
 * @description 描述
 */
class NotifyParentViewModel : ViewModel() {

    private val parentListLiveData = MutableLiveData<Result<List<ResponseParentBean>>>()
    val parentList = parentListLiveData

    /**
     * 请求通知人员- 家长
     * @param id Int
     * @param classesId String
     */
    fun requestStaffList(id: Int, classesId: String){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            map["classesId"] = classesId
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.requestParentList(body)
            parentListLiveData.value = result
        }
    }

}