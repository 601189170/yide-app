package com.yyide.chatim_pro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.model.schedule.TodaylistData

/**
 *
 * @author liu tao
 * @date 2021/10/9 11:47
 * @description 今日清单数据
 */
class TodayScheduleViewModel : ViewModel() {
    private val todayList = MutableLiveData<TodaylistData>()

    fun getTodayList(): LiveData<TodaylistData> {
        return todayList
    }

    fun getTodayScheduleList(){
        val todaylistData = TodaylistData()
        todaylistData.todayList = ScheduleDaoUtil.todayList()
        todaylistData.thisWeekUndoList = ScheduleDaoUtil.undoneOfWeek()
        todayList.postValue(todaylistData)
    }
}