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
import com.yyide.chatim.model.getSchoolWorkFeedback
import com.yyide.chatim.model.schedule.ScheduleData
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