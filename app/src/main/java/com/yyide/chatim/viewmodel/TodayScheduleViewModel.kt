package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.TodayListRsp
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
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)
    private val thisWeekUndoList = MutableLiveData<List<ScheduleData>?>()
    private val todayList = MutableLiveData<List<ScheduleData>?>()
    fun getThisWeekUndoList(): LiveData<List<ScheduleData>?> {
        return thisWeekUndoList
    }

    fun getTodayList(): LiveData<List<ScheduleData>?> {
        return todayList
    }

    fun getThisWeekAndTodayList(){
        apiStores.thisWeekAndTodayList.enqueue(object :Callback<TodayListRsp>{
            override fun onResponse(call: Call<TodayListRsp>, response: Response<TodayListRsp>) {
                val body = response.body()
                if (body != null && body.code == 200 && body.data != null){
                    val data = body.data
                    thisWeekUndoList.postValue(data?.thisWeekList)
                    todayList.postValue(data?.todayList)
                    return
                }
                thisWeekUndoList.postValue(null)
                todayList.postValue(null)
            }

            override fun onFailure(call: Call<TodayListRsp>, t: Throwable) {
                thisWeekUndoList.postValue(null)
                todayList.postValue(null)
            }
        })
    }
}