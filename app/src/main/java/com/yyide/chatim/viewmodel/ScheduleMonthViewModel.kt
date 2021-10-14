package com.yyide.chatim.viewmodel

import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.schedule.ScheduleListRsp
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/10/13 14:45
 * @description 日程月列表
 */
class ScheduleMonthViewModel : ViewModel() {
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)

    /**
     * 请求数据日程数据
     */
    fun scheduleList(startTime: String, endTime: String) {
        val requestData = mutableMapOf<String, String>()
        requestData["startTime"] = startTime
        requestData["endTime"] = endTime
        val toJSONString = JSON.toJSONString(requestData)
        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        apiStores.scheduleList(requestBody).enqueue(object : Callback<ScheduleListRsp> {
            override fun onResponse(
                call: Call<ScheduleListRsp>,
                response: Response<ScheduleListRsp>
            ) {
                val body = response.body()
                if (body != null && body.code ==200 && body.data != null){
                    val scheduleList = body.data.scheduleList

                    return
                }
            }

            override fun onFailure(call: Call<ScheduleListRsp>, t: Throwable) {

            }
        })
    }
}