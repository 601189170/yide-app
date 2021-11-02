package com.yyide.chatim.activity.weekly.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.model.WeeklyParentsAttendDetailBean
import com.yyide.chatim.model.WeeklyTeacherAttendanceBean
import com.yyide.chatim.model.WeeklyTeacherBean
import kotlinx.coroutines.launch

class ParentsAttendanceViewModel : BaseViewModel() {

    val parentsAttendanceLiveData = MutableLiveData<Result<WeeklyParentsAttendDetailBean>>()

    /**
    @Query("classId") classId: String,
    @Query("teacherId") teacherId: String,
    @Query("startTime") startTime: String,
    @Query("endTime") endTime: String
     */
    fun requestAttendanceTeacherDetail(
        studentId: String,
        startTime: String,
        endTime: String
    ) {
        viewModelScope.launch {
            val result =
                NetworkApi.requestParentsStudentDetail(studentId, startTime, endTime)
            parentsAttendanceLiveData.value = result
        }
    }

}