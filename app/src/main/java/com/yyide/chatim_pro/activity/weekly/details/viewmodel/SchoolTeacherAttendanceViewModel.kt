package com.yyide.chatim_pro.activity.weekly.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim_pro.base.BaseViewModel
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.SchoolWeeklyTeacherBean
import kotlinx.coroutines.launch

class SchoolTeacherAttendanceViewModel : BaseViewModel() {

    val schoolTeacherAttendanceLiveData = MutableLiveData<Result<SchoolWeeklyTeacherBean>>()

    fun requestSchoolTeacherAttendance(startTime: String, endTime: String) {
        viewModelScope.launch {
            val result = NetworkApi.requestSchoolTeacherAttendance(startTime, endTime)
            schoolTeacherAttendanceLiveData.value = result
        }
    }

}