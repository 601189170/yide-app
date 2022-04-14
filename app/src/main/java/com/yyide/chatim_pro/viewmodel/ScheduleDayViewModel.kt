package com.yyide.chatim_pro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.model.schedule.ScheduleData
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/10/13 14:45
 * @description 日程日列表
 */
class ScheduleDayViewModel : ViewModel() {
    val dayDataList:MutableLiveData<Map<DateTime, MutableList<ScheduleData>>> = MutableLiveData()
    /**
     * 请求月的日程数据 按日分组
     * @param dateTime 需要请求日程列表的时间 月
     */
    fun scheduleList(dateTime: DateTime){
        val map = mutableMapOf<DateTime, MutableList<ScheduleData>>()
        val monthlyList = ScheduleDaoUtil.monthlyList(dateTime,null)
        monthlyList.forEach {
            if (map.containsKey(it.dateTime)) {
                if (map[it.dateTime] == null) {
                    val mutableListOf = mutableListOf<ScheduleData>()
                    mutableListOf.add(it.scheduleData)
                    map[it.dateTime] = mutableListOf
                } else {
                    map[it.dateTime]?.add(it.scheduleData)
                }
            }else{
                val mutableListOf = mutableListOf<ScheduleData>()
                mutableListOf.add(it.scheduleData)
                map[it.dateTime] = mutableListOf
            }
        }
        dayDataList.postValue(map)
    }
}