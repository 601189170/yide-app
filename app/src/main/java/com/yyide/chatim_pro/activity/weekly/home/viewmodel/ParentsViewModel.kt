package com.yyide.chatim_pro.activity.weekly.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim_pro.base.BaseViewModel
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.WeeklyParentsBean
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