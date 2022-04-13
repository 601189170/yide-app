package com.yyide.chatim.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.TodoRsp
import com.yyide.chatim.model.UserSigRsp
import kotlinx.coroutines.launch
import okhttp3.RequestBody

/**
 * 首页预加载数据
 */
class MainViewModel : ViewModel() {
    val todoLiveData = MutableLiveData<Result<TodoRsp>>()

    private val imDataLiveData = MutableLiveData<Result<UserSigRsp.IMDataBean>>()
    val imData = imDataLiveData

    fun getTodoList() {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["pageNo"] = 1
            map["pageSize"] = 10
            map["status"] = 1  //1 待办
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.todoList(body)
            todoLiveData.value = result
        }
    }

    fun getUserSig(id: String) {
        viewModelScope.launch {
            val imDataResult = NetworkApi.requestIMSign(id)
            imDataLiveData.value = imDataResult
        }
    }

}