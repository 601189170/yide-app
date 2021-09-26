package com.yyide.chatim.activity.weekly.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.model.SchoolWeeklyData
import com.yyide.chatim.model.SchoolWeeklyTeacherBean
import kotlinx.coroutines.launch

class SchoolTeacherAttendanceViewModel : BaseViewModel() {

    val schoolTeacherAttendanceLiveData = MutableLiveData<Result<SchoolWeeklyTeacherBean>>()

    fun requestSchoolAttendance(startTime: String, endTime: String) {
        viewModelScope.launch {
            val result = NetworkApi.requestSchoolTeacherAttendance(startTime, endTime)
            schoolTeacherAttendanceLiveData.value = result
        }
    }

}