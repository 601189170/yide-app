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





    fun getSchoolWorkData(isWhole:String){
    viewModelScope.launch {
        val result = NetworkApi.getSchoolWorkData(isWhole)
        Log.e("TAG", "selectSubjectByUserId: "+JSON.toJSONString(result) )

        getSchoolWorkRsp.value = result
    }
}
}