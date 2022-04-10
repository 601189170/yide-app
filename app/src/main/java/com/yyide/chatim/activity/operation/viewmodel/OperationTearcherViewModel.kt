package com.yyide.chatim.activity.operation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.TeacherWorkListRsp
import com.yyide.chatim.model.getSchoolWork
import com.yyide.chatim.model.schedule.ScheduleData
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class OperationTearcherViewModel : ViewModel() {

    val getSchoolWorkRsp = MutableLiveData<Result<getSchoolWork>>()
    val deleteWorkResult = MutableLiveData<Result<Boolean>>()
    val updateRemindResult = MutableLiveData<Result<Boolean>>()

    fun getSchoolWorkData(isWhole:String){
    viewModelScope.launch {
        val result = NetworkApi.getSchoolWorkData(isWhole)
        getSchoolWorkRsp.value = result
    }
    }


    fun deleteWork(isWhole:String){
        viewModelScope.launch {
            val result = NetworkApi.deleteWork(isWhole)
            deleteWorkResult.value = result
        }
    }

    fun updateRemind(isWhole:String,workId:String,classesId:String,studentId:String){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["isWhole"] = isWhole
            map["workId"] = workId
            map["classesId"] = classesId
            map["studentId"] = studentId
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.updateRemind(body)
            updateRemindResult.value = result
        }
    }

}