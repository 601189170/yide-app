package com.yyide.chatim.activity.weekly.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.SchoolWeeklyData
import kotlinx.coroutines.launch

class SchoolViewModel : BaseViewModel() {

    val schoolLiveData = MutableLiveData<Result<SchoolWeeklyData>>()

    fun requestSchoolWeekly(startTime: String, endTime: String) {
        viewModelScope.launch {
            val result = NetworkApi.schoolWeekly(startTime, endTime)
            schoolLiveData.value = result
        }
    }

}