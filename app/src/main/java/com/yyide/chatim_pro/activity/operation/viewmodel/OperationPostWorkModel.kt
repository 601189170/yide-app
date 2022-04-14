package com.yyide.chatim_pro.activity.operation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class OperationPostWorkModel : ViewModel() {

    val getWeekTimeRsp = MutableLiveData<Result<getWeekTimeRsp>>()

    val subjectData = MutableLiveData<Result<List<selectSubjectByUserIdRsp>>>()

    val ClassListData = MutableLiveData<Result<List<getClassList>>>()

    val UploadRsp = MutableLiveData<Result<List<UploadRsp>>>()

    val ispost = MutableLiveData<Result<String>>()

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

    fun upPohto(files: List<File>){

        viewModelScope.launch {


//            val fileRequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//
//            val requestImgPart = MultipartBody.Part.createFormData("file", "fileName.jpg", fileRequestBody)


            val parts: MutableList<MultipartBody.Part> = ArrayList()
            for (file in files) {
                // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
                val requestBody = RequestBody.create("image/png".toMediaTypeOrNull(), file)
                val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
                parts.add(part)
            }
            val result = NetworkApi.upPohto(parts)
            UploadRsp.value = result
        }
    }
}