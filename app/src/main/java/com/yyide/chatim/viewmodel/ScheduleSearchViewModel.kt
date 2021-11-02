package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.schedule.FilterTagCollect
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.loge
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/10/12 11:19
 * @description 描述
 */
class ScheduleSearchViewModel : ViewModel() {
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)
    val scheduleSearchResultList = MutableLiveData<List<ScheduleData>>()
    fun getScheduleSearchResultList(): LiveData<List<ScheduleData>> {
        return scheduleSearchResultList
    }

    /**
     * 搜索日程
     */
    fun searchSchedule(filterTagCollect: FilterTagCollect) {
        searchLocalSchedule(filterTagCollect)
        val toJSONString = JSON.toJSONString(filterTagCollect)
        loge("搜索日程：$toJSONString")
//        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
//        apiStores.searchSchedule(requestBody)
//            .enqueue(object : Callback<BaseResponse<List<ScheduleData>>> {
//                override fun onResponse(
//                    call: Call<BaseResponse<List<ScheduleData>>>,
//                    response: Response<BaseResponse<List<ScheduleData>>>
//                ) {
//                    val body = response.body()
//                    if (body != null && body.code == 200 && body.data != null) {
//                        scheduleSearchResultList.postValue(body.data ?: listOf())
//                        return
//                    }
//                    scheduleSearchResultList.postValue(listOf())
//                }
//
//                override fun onFailure(call: Call<BaseResponse<List<ScheduleData>>>, t: Throwable) {
//                    scheduleSearchResultList.postValue(listOf())
//                }
//            })
    }

    /**
     * 搜索本地日程 根据赛选条件
     */
    fun searchLocalSchedule(filterTagCollect: FilterTagCollect) {
        if (filterTagCollect.status?.isEmpty() == true){
            filterTagCollect.status = listOf(0,1)
        }

        if (filterTagCollect.type?.isEmpty() == true){
            filterTagCollect.type = listOf(2,0,3,1)
        }

        val filterOfSearchSchedule = ScheduleDaoUtil.filterOfSearchSchedule(filterTagCollect)
        scheduleSearchResultList.postValue(filterOfSearchSchedule)
    }
}