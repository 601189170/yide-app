package com.yyide.chatim.activity.weekly.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.model.WeeklyParentsBean
import kotlinx.coroutines.launch

class ParentsViewModel : BaseViewModel() {

    val parentsLiveData = MutableLiveData<Result<WeeklyParentsBean>>()

    fun requestParentsWeekly(studentId: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            val result = NetworkApi.parentsWeekly(studentId, startTime, endTime)
            parentsLiveData.value = result
        }
    }

}