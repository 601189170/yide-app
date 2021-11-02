package com.yyide.chatim.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.model.schedule.DayGroupOfMonth
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.ScheduleListRsp
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import okhttp3.RequestBody
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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