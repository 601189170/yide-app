package com.yyide.chatim_pro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.model.schedule.SchoolCalendarRsp
import com.yyide.chatim_pro.model.schedule.SchoolSemesterRsp

import com.yyide.chatim_pro.net.AppClient
import com.yyide.chatim_pro.net.DingApiStores
import com.yyide.chatim_pro.utils.logd
import com.yyide.chatim_pro.utils.loge
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/11/29 13:57
 * @description 描述
 */
class SchoolCalendarViewModel : ViewModel() {
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)
    val semesterList = MutableLiveData<List<SchoolSemesterRsp.Data>>()

    val schoolCalendarList = MutableLiveData<List<SchoolCalendarRsp.DataBean>>()

    /**
     * 查询校历学年学期
     */
    fun selectSemester() {
        apiStores.selectSemester().enqueue(object : Callback<SchoolSemesterRsp> {
            override fun onResponse(
                call: Call<SchoolSemesterRsp>,
                response: Response<SchoolSemesterRsp>
            ) {
                val body = response.body()
                logd("$body")
                if (body != null && body.code == 0 && body.data != null) {
                    semesterList.postValue(body.data ?: listOf())
                } else {
                    semesterList.postValue(listOf())
                }
            }

            override fun onFailure(call: Call<SchoolSemesterRsp>, t: Throwable) {
                semesterList.postValue(listOf())
            }
        })
    }

    /**
     * 查询校历备注数据
     */
    fun selectSchoolCalendar(id: String?, dayOfMonth: String?) {
        if (id == null || dayOfMonth == null){
            schoolCalendarList.postValue(listOf())
            loge("id or dayOfMonth is null")
            return
        }
        val map = HashMap<String, Any>()
//        map["id"] = "1461949270799777793"
        map["id"] = id
        map["dayOfMonth"] = dayOfMonth
        val toJSONString = JSON.toJSONString(map)
        loge("查询校历：$toJSONString")
        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        apiStores.selectSchoolCalendar(requestBody).enqueue(object : Callback<SchoolCalendarRsp> {
            override fun onResponse(
                call: Call<SchoolCalendarRsp>,
                response: Response<SchoolCalendarRsp>
            ) {
                val body = response.body()
                logd("$body")
                if (body != null && body.code == 0 && body.data != null) {
                    schoolCalendarList.postValue(body.data ?: listOf())
                } else {
                    schoolCalendarList.postValue(listOf())
                }
            }

            override fun onFailure(call: Call<SchoolCalendarRsp>, t: Throwable) {
                schoolCalendarList.postValue(listOf())
            }

        })
    }
}