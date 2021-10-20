package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.model.schedule.StudentGuardianRsp
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

    fun getResponseResult(): LiveData<ParticipantRsp.DataBean?> {
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
                    val data = body.data
                    data.participantList?.let {
                        it.forEach {
                            it.name = it.realname
                            it.realname = it.realname
                        }
                    }
                    responseResult.postValue(data)
                } else {
                    responseResult.postValue(null)
                }
            }
        })
    }

    /**
     * 查询学生或家长
     */
    fun getStudentGuardianParticipant(id: String = "", type: String = "0", scope: String = "1") {
        apiStores.getParticipant(id, type, scope).enqueue(object : Callback<StudentGuardianRsp> {
            override fun onResponse(
                call: Call<StudentGuardianRsp>,
                response: Response<StudentGuardianRsp>
            ) {
                val body = response.body()
                if (body != null && body.code == 200 && body.data != null) {
                    val data = body.data
                    val dataBean = ParticipantRsp.DataBean()

                    val departmentList =
                        mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
                    val participantList =
                        mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
                    dataBean.name = data.name
                    data.childList?.list?.forEach {
                        val participantListBean =
                            ParticipantRsp.DataBean.ParticipantListBean()
                        participantListBean.department = true
                        participantListBean.checked = false
                        participantListBean.type = it.type
                        participantListBean.name = it.name
                        participantListBean.id = it.id
                        departmentList.add(participantListBean)
                    }

                    data.participantList?.forEach {
                        val participantListBean =
                            ParticipantRsp.DataBean.ParticipantListBean()
                        participantListBean.id = it.id
                        participantListBean.userId = it.userId
                        participantListBean.name = it.realname
                        participantListBean.realname = it.realname
                        //participantListBean.userName = it.realname
                        participantListBean.department = false
                        participantListBean.checked = false
                        participantList.add(participantListBean)
                    }

                    if (participantList.isNotEmpty() || departmentList.isNotEmpty()) {
                        //学生或家长
                        dataBean.departmentList = departmentList
                        dataBean.participantList = participantList
                        responseResult.postValue(dataBean)
                    } else {
                        responseResult.postValue(null)
                    }

                } else {
                    responseResult.postValue(null)
                }
            }

            override fun onFailure(call: Call<StudentGuardianRsp>, t: Throwable) {
                responseResult.postValue(null)
            }

        })
    }
}