package com.yyide.chatim.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.schedule.*
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
 * @description 日程列表视图
 */
class ScheduleListViewViewModel : ViewModel() {
    val listViewData:MutableLiveData<List<MonthViewScheduleData>> = MutableLiveData()
    /**
     * 请求列表视图的日程数据 按日分组
     * @param dateTime 需要请求日程列表的时间 月
     */
    fun scheduleList(dateTime: DateTime){
        val map = mutableMapOf<DateTime, MutableList<ScheduleData>>()
        val monthlyList = ScheduleDaoUtil.monthlyList(dateTime)
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
        listViewData.postValue(list2)
    }

}