package com.yyide.chatim.activity.operation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class OperationViewModelByParents : ViewModel() {


    val StudentDatasList = MutableLiveData<Result<List<selectParentStudent>>>()

    val ParentsWorkDatasList = MutableLiveData<Result<ParentWorkList>>()

    val rightData = MutableLiveData<rightData>()


    val subjectId = MutableLiveData<String>()

    val classesId = MutableLiveData<String>()

    val studentId = MutableLiveData<String>()

    val startTime = MutableLiveData<String>()

    val endTime = MutableLiveData<String>()

    val allDayLiveData = MutableLiveData<Boolean>(false)

    fun getStudentDatas(){
        viewModelScope.launch {
            val result = NetworkApi.selectParentStudent()
            StudentDatasList.value = result
        }
    }

    fun selectParentPage(subjectId: String, studentId: String,classesId:String,startTime: String,endTime :String,pageNo:Int,pageSize:Int){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()

            map["subjectId"] = subjectId
            map["studentId"] = studentId
            map["classesId"] = classesId
            map["startTime"] = startTime
            map["endTime"] = endTime
            map["pageNo"] = pageNo
            map["pageSize"] = pageSize
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.selectParentPage(body)
            ParentsWorkDatasList.value = result
        }
    }
}