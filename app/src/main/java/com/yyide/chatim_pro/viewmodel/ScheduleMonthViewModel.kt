package com.yyide.chatim_pro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim_pro.model.schedule.*
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil.simplifiedToMonthOfDateTime
import com.yyide.chatim_pro.utils.loge
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/10/13 14:45
 * @description 日程月列表
 */
class ScheduleMonthViewModel : ViewModel() {
    val monthDataList:MutableLiveData<Map<DateTime, MutableList<ScheduleData>>> = MutableLiveData()

    val listViewData:MutableLiveData<List<ScheduleData>> = MutableLiveData()
    //更新日程列表时
    val updateListViewData:MutableLiveData<List<ScheduleData>> = MutableLiveData()
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
        monthDataList.postValue(map)
    }


    private fun scheduleList2(dateTime: DateTime,timeAxisDateTime:DateTime?,moreThanMonth:Boolean):List<MonthViewScheduleData>{
        val map = mutableMapOf<DateTime, MutableList<ScheduleData>>()
        val monthlyList = ScheduleDaoUtil.monthlyList(dateTime,timeAxisDateTime,moreThanMonth = moreThanMonth)
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
        val list = mutableListOf<DayViewScheduleData>()
        map.keys.forEach {
            list.add(DayViewScheduleData(it,map[it]?: mutableListOf()))
        }
        val list2 = mutableListOf<MonthViewScheduleData>()
        if (list.isNotEmpty()){
            //list可能跨几个月，需要按月分组
            val mapMonth = mutableMapOf<String,MutableList<DayViewScheduleData>>()
            list.forEach {
                val dateTimeKey = it.dateTime.simplifiedToMonthOfDateTime().toStringTime()
                if (mapMonth.containsKey(dateTimeKey)) {
                    if (mapMonth[dateTimeKey] == null) {
                        val mutableListOf = mutableListOf<DayViewScheduleData>()
                        mutableListOf.add(it)
                        mapMonth[dateTimeKey] = mutableListOf
                    } else {
                        mapMonth[dateTimeKey]?.add(it)
                    }
                } else {
                    val mutableListOf = mutableListOf<DayViewScheduleData>()
                    mutableListOf.add(it)
                    mapMonth[dateTimeKey] = mutableListOf
                }
            }

            mapMonth.keys.forEach {
                val simplifiedDataTime = ScheduleDaoUtil.toDateTime(it).simplifiedToMonthOfDateTime()
                list2.add(MonthViewScheduleData(simplifiedDataTime,mapMonth[it]?: mutableListOf()))
            }
        }
        return list2
    }
    fun scheduleDataList(dateTime: DateTime,timeAxisDateTime:DateTime?,loadMore:Boolean = true,moreThanMonth:Boolean = false){
        val scheduleList = scheduleList2(dateTime, timeAxisDateTime,moreThanMonth)
        val scheduleDataList = mutableListOf<ScheduleData>()
        scheduleList.forEach {
            //月开始
            val scheduleData = ScheduleData()
            scheduleData.isMonthHead = true
            scheduleData.isFirstDayOfMonth = false
            scheduleData.isTimeAxis = false
            scheduleData.startTime = it.dateTime.toStringTime()
            scheduleDataList.add(scheduleData)
            it.list.forEach {
                for (scheduleDataIndex in it.list.indices) {

                    if ((scheduleDataIndex == 0 && !it.list[0].isTimeAxis) || (scheduleDataIndex == 1 && it.list[0].isTimeAxis)){
                        //日开始
                        val scheduleData1 = it.list[scheduleDataIndex]
                        scheduleData1.isMonthHead = false
                        scheduleData1.isFirstDayOfMonth = true
                        scheduleData1.isTimeAxis = false
                        scheduleDataList.add(scheduleData1)
                        continue
                    }

                    val scheduleData1 = it.list[scheduleDataIndex]
                    scheduleData1.isFirstDayOfMonth = false
                    scheduleData1.isMonthHead = false
                    scheduleData1.isTimeAxis = scheduleData1.isTimeAxis
                    scheduleDataList.add(scheduleData1)
                }
            }
        }

        loge("日程列表数据=========${JSON.toJSONString(scheduleDataList)}")
        if (loadMore) {
            listViewData.postValue(scheduleDataList)
        } else {
            updateListViewData.postValue(scheduleDataList)
        }

    }
}