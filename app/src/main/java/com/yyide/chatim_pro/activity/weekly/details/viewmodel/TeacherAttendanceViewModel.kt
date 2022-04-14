package com.yyide.chatim_pro.activity.weekly.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim_pro.base.BaseViewModel
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.WeeklyTeacherAttendanceBean
import kotlinx.coroutines.launch

class TeacherAttendanceViewModel : BaseViewModel() {

    val teacherAttendanceLiveData = MutableLiveData<Result<WeeklyTeacherAttendanceBean>>()

    /**
    @Query("classId") classId: String,
    @Query("teacherId") teacherId: String,
    @Query("startTime") startTime: String,
    @Query("endTime") endTime: String
     */
    fun requestAttendanceTeacherDetail(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) {
        viewModelScope.launch {
            val result =
                NetworkApi.requestAttendanceTeacherDetail(classId, teacherId, startTime, endTime)
            teacherAttendanceLiveData.value = result
        }
    }

}