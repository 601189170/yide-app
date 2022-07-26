package com.yyide.chatim_pro.activity.attendance.teacher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyide.chatim_pro.kotlin.network.attendance.AttendanceNetwork
import com.yyide.chatim_pro.model.attendance.teacher.DailyRecordItem
import com.yyide.chatim_pro.model.attendance.teacher.MonthDayBean
import com.yyide.chatim_pro.model.attendance.teacher.TeacherStatisticsMonthBean
import kotlinx.coroutines.launch

/**
 *
 * @author shenzhibin
 * @date 2022/3/30 15:16
 * @description 教师考勤统计的viewModel
 */
class TeacherStatisticsViewModel : ViewModel() {


    private val monthRecordLiveData = MutableLiveData<Result<TeacherStatisticsMonthBean>>()
    val monthRecordData = monthRecordLiveData

    private val dayRecordLiveData = MutableLiveData<Result<DailyRecordItem>>()
    val dayRecordData = dayRecordLiveData

    private val dateLiveData = MutableLiveData<MonthDayBean>()
    val date = dateLiveData

    val dailyRecordList = mutableListOf<DailyRecordItem>()

    /**
     * 查询月度考勤信息
     */
    fun requestClockRecordByMonth(month: String) {
        viewModelScope.launch {
            val result = AttendanceNetwork.requestClockRecordByMonth(month)
            monthRecordLiveData.value = result
        }
    }

    /**
     * 查询日考勤信息
     */
    fun requestClockRecordByDay(day: String) {
        viewModelScope.launch {
            val result = AttendanceNetwork.requestClockRecordByDay(day)
            dayRecordLiveData.value = result
        }
    }

    /**
     * 设置时间
     * @param date MonthDayBean
     */
    fun setDate(date: MonthDayBean) {
        dateLiveData.value = date
    }

}