package com.yyide.chatim.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.loge
import okhttp3.RequestBody
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/10/13 14:45
 * @description 日程列表视图
 */
class ScheduleListViewViewModel : ViewModel() {
    val listViewData:MutableLiveData<List<ScheduleData>> = MutableLiveData()
    /**
     * 请求列表视图的日程数据 按日分组
     * @param dateTime 需要请求日程列表的时间 月
     */
    private fun scheduleList(dateTime: DateTime,timeAxisDateTime:DateTime?):List<MonthViewScheduleData>{
        val map = mutableMapOf<DateTime, MutableList<ScheduleData>>()
        val monthlyList = ScheduleDaoUtil.monthlyList(dateTime,timeAxisDateTime)
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
            list2.add(MonthViewScheduleData(dateTime,list))
        }
        return list2
    }

    /**
     * 获取日程列表数据
     */
    fun scheduleDataList(dateTime: DateTime,timeAxisDateTime:DateTime?){
        val scheduleList = scheduleList(dateTime, timeAxisDateTime)
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
        listViewData.postValue(scheduleDataList)
        loge("日程列表数据=========${JSON.toJSONString(scheduleDataList)}")
    }

}