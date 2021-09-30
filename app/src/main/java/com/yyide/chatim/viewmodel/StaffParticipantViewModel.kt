package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/9/29 13:42
 * @description 描述
 */
class StaffParticipantViewModel : ViewModel() {
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)

    //参与人层级列表
    private val participantList: MutableLiveData<MutableList<ParticipantRsp.DataBean>> by lazy {
        MutableLiveData(mutableListOf())
    }

    fun getParticipantList(): LiveData<MutableList<ParticipantRsp.DataBean>> {
        return participantList
    }

    //当前参与人列表
   val curParticipantList: MutableLiveData<MutableList<ParticipantRsp.DataBean.ParticipantListBean>> by lazy {
        MutableLiveData(mutableListOf())
    }

    private val responseResult: MutableLiveData<ParticipantRsp.DataBean> by lazy {
        MutableLiveData()
    }

    fun getResponseResult() :LiveData<ParticipantRsp.DataBean?>{
        return responseResult
    }

    fun getTeacherParticipant(departmentId: String = "") {
        apiStores.getTeacherParticipant(departmentId).enqueue(object : Callback<ParticipantRsp> {
            override fun onFailure(call: Call<ParticipantRsp>, t: Throwable) {
                responseResult.postValue(null)
            }

            override fun onResponse(
                call: Call<ParticipantRsp>,
                response: Response<ParticipantRsp>
            ) {
                val body = response.body()
                if (body != null && body.code == 200 && body.data != null) {
                    responseResult.postValue(body.data)
                } else {
                    responseResult.postValue(null)
                }
            }
        })
    }
}