package com.yyide.chatim_pro.activity.operation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.getSchoolWorkFeedback
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class OperationParentsViewModel : ViewModel() {

    val ParentsWorkInfoData = MutableLiveData<Result<getSchoolWorkFeedback>>()

    val ispost = MutableLiveData<Result<Boolean>>()





    /**
     * 家长-作业详情
     */
    fun getParensWorkInfo(id:String){
    viewModelScope.launch {
        val result = NetworkApi.getParensWorkInfo(id)
        ParentsWorkInfoData.value = result
    }
    }
    /**
     * 家长-提交反馈
     */
    fun CommitFeedback(id:String,completion:Int,difficulty:Int,isFeedback:Boolean){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = id
            map["completion"] = completion
            map["difficulty"] = difficulty
            map["isFeedback"] = isFeedback
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.CommitFeedback(body)
            ispost.value = result
        }
    }

}