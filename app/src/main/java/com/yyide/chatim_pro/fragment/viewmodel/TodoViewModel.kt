package com.yyide.chatim_pro.fragment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.TodoRsp
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class TodoViewModel : ViewModel() {
    val todoLiveData = MutableLiveData<Result<TodoRsp>>()
    val leaveRefuseOrPassLiveData = MutableLiveData<Result<String>>()

    fun getTodoList(pageNo: Int, pageSize: Int, status: String) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["pageNo"] = pageNo
            map["pageSize"] = pageSize
            map["status"] = status
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.todoList(body)
            todoLiveData.value = result
        }
    }

    fun leaveRefuseOrPass(taskId: String, outcome: String, comment: String) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["taskId"] = taskId
            map["outcome"] = outcome
            map["comment"] = comment
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.leaveRefuseOrPass(body)
            leaveRefuseOrPassLiveData.value = result
        }
    }
}