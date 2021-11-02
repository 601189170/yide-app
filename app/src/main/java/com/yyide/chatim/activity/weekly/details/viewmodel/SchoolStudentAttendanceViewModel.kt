package com.yyide.chatim.activity.weekly.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.SchoolWeeklyTeacherBean
import kotlinx.coroutines.launch

class SchoolStudentAttendanceViewModel : BaseViewModel() {

    val schoolStudentAttendanceLiveData = MutableLiveData<Result<SchoolWeeklyTeacherBean>>()

    fun requestSchoolAttendance(startTime: String, endTime: String) {
        viewModelScope.launch {
            val result = NetworkApi.requestSchoolStudentAttendance(startTime, endTime)
            schoolStudentAttendanceLiveData.value = result
        }
    }

}