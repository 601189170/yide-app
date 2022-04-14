package com.yyide.chatim_pro.activity.attendance.teacher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.kotlin.network.attendance.AttendanceNetwork
import com.yyide.chatim_pro.model.UserBean
import com.yyide.chatim_pro.model.attendance.teacher.TeacherAttendanceRuleBean
import kotlinx.coroutines.launch

/**
 *
 * @author shenzhibin
 * @date 2022/3/30 16:58
 * @description 教师考勤规则ViewModel
 */
class AttendanceRuleViewModel : ViewModel() {

    val userInfo : UserBean = SpData.getUser()

    private val attendanceRuleLiveData = MutableLiveData<TeacherAttendanceRuleBean>()
    val attendanceRule = attendanceRuleLiveData


    /**
     * 查询考勤规则
     */
    fun queryAttendanceRule() {
        viewModelScope.launch {
            val queryResult = AttendanceNetwork.requestAttendanceRule()
            val queryData = queryResult.getOrNull()
            queryData?.let {
                attendanceRuleLiveData.value = it
            }
        }
    }

}