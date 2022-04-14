package com.yyide.chatim_pro.activity.weekly.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim_pro.base.BaseViewModel
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.WeeklyTeacherBean
import kotlinx.coroutines.launch

class TeacherViewModel : BaseViewModel() {

    val teacherLiveData = MutableLiveData<Result<WeeklyTeacherBean>>()

    /**
    @Query("classId") classId: String,
    @Query("teacherId") teacherId: String,
    @Query("startTime") startTime: String,
    @Query("endTime") endTime: String
     */
    fun requestTeacherWeekly(
        classId: String,
        teacherId: String,
        startTime: String,
        endTime: String
    ) {
        viewModelScope.launch {
            val result = NetworkApi.teacherWeekly(classId, teacherId, startTime, endTime)
            teacherLiveData.value = result
        }
    }

}