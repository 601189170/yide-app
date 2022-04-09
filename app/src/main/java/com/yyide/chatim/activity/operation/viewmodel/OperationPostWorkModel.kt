package com.yyide.chatim.activity.operation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.CreateWorkBean
import com.yyide.chatim.model.getClassList
import com.yyide.chatim.model.getWeekTimeRsp
import com.yyide.chatim.model.selectSubjectByUserIdRsp
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class OperationPostWorkModel : ViewModel() {

    val getWeekTimeRsp = MutableLiveData<Result<getWeekTimeRsp>>()

    val subjectData = MutableLiveData<Result<List<selectSubjectByUserIdRsp>>>()

    val ClassListData = MutableLiveData<Result<List<getClassList>>>()

    val ispost = MutableLiveData<Result<Boolean>>()

    val subjectId = MutableLiveData<String>()

    val startTimeLiveData = MutableLiveData<String>()

    val endTimeLiveData = MutableLiveData<String>()

    //是否是全天日程
    val allDayLiveData = MutableLiveData<Boolean>(false)

    fun getWeekTime(type:String,typeId:String,weekTime:String,subjectId:String){

        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["type"] = type
            map["typeId"] = typeId
            map["weekTime"] = weekTime
            map["subjectId"] = subjectId
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.getWeekTime(body)
            getWeekTimeRsp.value = result
        }
    }

    fun getSubject(){

        viewModelScope.launch {

            val result = NetworkApi.getSubject()
            subjectData.value = result
        }
    }


    fun getClassList(){

        viewModelScope.launch {

            val result = NetworkApi.getClassList()
            ClassListData.value = result
        }
    }

    fun createWork(bean: CreateWorkBean){

        viewModelScope.launch {
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(bean))
            val result = NetworkApi.createWork(body)
            ispost.value = result
        }
    }
}