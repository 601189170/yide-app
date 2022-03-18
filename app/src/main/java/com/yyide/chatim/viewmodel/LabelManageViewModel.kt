package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.NewLabel
import com.yyide.chatim.model.schedule.OldLabel
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
 * @date 2021/9/22 14:08
 * @description 描述
 */
class LabelManageViewModel : ViewModel() {
    private val labelList: MutableLiveData<List<LabelListRsp.DataBean>> = MutableLiveData()
    private val labelAddOrEditResult: MutableLiveData<Boolean> = MutableLiveData()
    private val labelDeleteResult: MutableLiveData<Boolean> = MutableLiveData()
    private var dingApiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)

    fun getLabelList(): LiveData<List<LabelListRsp.DataBean>> {
        return labelList
    }

    fun getLabelAddOrEditResult(): LiveData<Boolean> {
        return labelAddOrEditResult
    }

    fun getLabelDeleteResult(): LiveData<Boolean> {
        return labelDeleteResult
    }

    init {
        selectLabelList()
    }

    /**
     * 查询标签
     */
    fun selectLabelList() {
        dingApiStores.selectLabelList().enqueue(object : Callback<LabelListRsp> {
            override fun onResponse(call: Call<LabelListRsp>, response: Response<LabelListRsp>) {
                loge("selectLabelList:${response.body()}")
                val body = response.body()
                if (body != null) {
                    val data = body.data
                    labelList.postValue(data)
                    return
                }
                labelList.postValue(listOf())
            }

            override fun onFailure(call: Call<LabelListRsp>, t: Throwable) {
                labelList.postValue(listOf())
            }
        })
    }

    /**
     * 新建标签
     */
    fun addLabel(labels: List<NewLabel>) {
        val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(labels))
        dingApiStores.addLabel(body).enqueue(object : Callback<BaseRsp> {
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                loge("addLabel:${response.body()}")
                val baseRsp: BaseRsp? = response.body()
                if (baseRsp != null && baseRsp.code == BaseConstant.REQUEST_SUCCES_0) {
                    labelAddOrEditResult.postValue(true)
                    return
                }
                labelAddOrEditResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                labelAddOrEditResult.postValue(false)
            }
        })
    }

    /**
     * 修改便签
     */
//    fun editLabel(oldLabel: OldLabel) {
//        val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(oldLabel))
//        dingApiStores.editLabel(body).enqueue(object : Callback<BaseRsp> {
//            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
//                loge("editLabel:${response.body()}")
//                val baseRsp: BaseRsp? = response.body()
//                if (baseRsp != null && baseRsp.code == BaseConstant.REQUEST_SUCCES_0) {
//                    labelAddOrEditResult.postValue(true)
//                    return
//                }
//                labelAddOrEditResult.postValue(false)
//            }
//
//            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
//                labelAddOrEditResult.postValue(false)
//            }
//        })
//    }
    fun editLabel(oldLabel: ArrayList<OldLabel>) {
        val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(oldLabel))
        dingApiStores.editLabel(body).enqueue(object : Callback<BaseRsp> {
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                loge("editLabel:${response.body()}")
                val baseRsp: BaseRsp? = response.body()
                if (baseRsp != null && baseRsp.code == BaseConstant.REQUEST_SUCCES_0) {
                    labelAddOrEditResult.postValue(true)
                    return
                }
                labelAddOrEditResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                labelAddOrEditResult.postValue(false)
            }
        })
    }
    /**
     * 删除标签
     */
    fun deleteLabelById(id: String) {
        loge("删除标签 deleteLabelById=$id")
        dingApiStores.deleteLabelById(id).enqueue(object : Callback<BaseRsp> {
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                loge("deleteLabelById:${response.body()}")
                val baseRsp: BaseRsp? = response.body()
                if (baseRsp != null && baseRsp.code == BaseConstant.REQUEST_SUCCES_0) {
                    labelDeleteResult.postValue(true)
                    return
                }
                labelDeleteResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                labelDeleteResult.postValue(false)
            }
        })
    }
}