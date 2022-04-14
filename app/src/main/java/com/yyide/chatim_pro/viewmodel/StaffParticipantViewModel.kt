package com.yyide.chatim_pro.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.model.schedule.ParticipantRsp
import com.yyide.chatim_pro.model.schedule.SearchParticipantRsp
import com.yyide.chatim_pro.model.schedule.StudentGuardianRsp
import com.yyide.chatim_pro.net.AppClient
import com.yyide.chatim_pro.net.DingApiStores
import com.yyide.chatim_pro.utils.loge
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

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
        val map = HashMap<String, Any>(2)
        map["id"] = departmentId
        val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
        apiStores.getTeacherParticipant(body).enqueue(object : Callback<ParticipantRsp> {
            override fun onFailure(call: Call<ParticipantRsp>, t: Throwable) {
                responseResult.postValue(null)
            }

            override fun onResponse(
                call: Call<ParticipantRsp>,
                response: Response<ParticipantRsp>
            ) {
                val body = response.body()
                if (body != null && body.code == BaseConstant.REQUEST_SUCCES_0 && body.data != null) {
                    val data = body.data
                    if (data.size>0){
                        data[0].list?.let {
                            it.forEach {
                                it.name = it.name
                                it.realname = it.realname
                            }
                        }
                        responseResult.postValue(data[0])
                    }

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
        val map = HashMap<String, Any>(2)
        map["id"] = id
        map["type"] = type
        map["scope"] = scope
        val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
        apiStores.getParticipant(body).enqueue(object : Callback<StudentGuardianRsp> {
            override fun onResponse(
                call: Call<StudentGuardianRsp>,
                response: Response<StudentGuardianRsp>
            ) {
                val body = response.body()
                if (body != null && body.code == BaseConstant.REQUEST_SUCCES_0 && body.data != null) {
                    val data = body.data
                    val dataBean = ParticipantRsp.DataBean()

                    val departmentList =
                        mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
                    val participantList =
                        mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
                    dataBean.name = data.name

                    data.list?.forEach {

                        val participantListBean =
                            ParticipantRsp.DataBean.ParticipantListBean()

                            participantListBean.department = true

                        participantListBean.checked = false
                        participantListBean.type = it.type
                        participantListBean.name = it.name
                        participantListBean.id = it.id
                        departmentList.add(participantListBean)
//                        if (it.guardians!=null){
//                            Log.e("TAG", "guardians: "+it.guardians )
//                        }
                        it.participantList?.forEach {
                            val participantListBean =
                                    ParticipantRsp.DataBean.ParticipantListBean()
                            participantListBean.id = it.id
                            participantListBean.userId = it.userId
                            participantListBean.name = it.name
                            participantListBean.realname = it.realname
                            //participantListBean.userName = it.realname
                            participantListBean.department = false
                            participantListBean.checked = false
                            participantListBean.guardians = it.guardians
                            participantList.add(participantListBean)
                        }
                    }

//                    data.participantList?.forEach {
//                        val participantListBean =
//                            ParticipantRsp.DataBean.ParticipantListBean()
//                        participantListBean.id = it.id
//                        participantListBean.userId = it.userId
//                        participantListBean.name = it.name
//                        participantListBean.realname = it.realname
//                        //participantListBean.userName = it.realname
//                        participantListBean.department = false
//                        participantListBean.checked = false
//                        participantListBean.guardians = it.guardians
//                        participantList.add(participantListBean)
//                    }

                    if (participantList.isNotEmpty() || departmentList.isNotEmpty()) {
                        //学生或家长
                        dataBean.list = departmentList
                        dataBean.personList = participantList
                        Log.e("TAG", "responseResult.postValue: "+JSON.toJSONString(dataBean) )
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

    //教职工搜索列表
    val teacherList: MutableLiveData<List<SearchParticipantRsp.DataBean.TeacherListBean>> by lazy {
        MutableLiveData<List<SearchParticipantRsp.DataBean.TeacherListBean>>()
    }

    //家长搜索列表
    val studentList: MutableLiveData<List<SearchParticipantRsp.DataBean.StudentListBean>> by lazy {
        MutableLiveData<List<SearchParticipantRsp.DataBean.StudentListBean>>()
    }

    /**
     * 查询参与人
     */
    fun searchParticipant(keyword: String) {
        val parameterMap = mutableMapOf<String, String>()
        parameterMap.put("name", keyword)
        val toJSONString = JSON.toJSONString(parameterMap)
        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        apiStores.searchParticipant(requestBody).enqueue(object : Callback<SearchParticipantRsp> {
            override fun onResponse(
                call: Call<SearchParticipantRsp>,
                response: Response<SearchParticipantRsp>
            ) {
                val body = response.body()
                loge("查询参与人结果：$body")
                if (body != null && body.code == BaseConstant.REQUEST_SUCCES_0 && body.data != null) {
                    //查询参与人成功
                    body.data?.let {
                        teacherList.postValue(it.personList?: listOf())
                        studentList.postValue(it.list?: listOf())
                    }
                    return
                }
                teacherList.postValue(null)
                studentList.postValue(null)
                if (!TextUtils.isEmpty(body?.msg)) {
                    ToastUtils.showShort(body?.msg)
                }
            }

            override fun onFailure(call: Call<SearchParticipantRsp>, t: Throwable) {
                loge("查询参与人结果：${t.localizedMessage}")
                teacherList.postValue(null)
                studentList.postValue(null)
                ToastUtils.showShort("查询失败")
            }
        })
    }
}