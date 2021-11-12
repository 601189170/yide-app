package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.TodayListRsp
import com.yyide.chatim.model.schedule.TodaylistData
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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