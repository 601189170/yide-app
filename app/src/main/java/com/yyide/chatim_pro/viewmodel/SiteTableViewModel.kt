package com.yyide.chatim_pro.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.base.BaseResponse
import com.yyide.chatim_pro.model.sitetable.SiteTableRsp
import com.yyide.chatim_pro.model.table.ClassInfoBean
import com.yyide.chatim_pro.net.AppClient
import com.yyide.chatim_pro.net.DingApiStores
import com.yyide.chatim_pro.utils.loge
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/12/9 11:26
 * @description 描述
 */
class SiteTableViewModel : ViewModel() {
    private var apiStores: DingApiStores = AppClient.getDingRetrofit().create(DingApiStores::class.java)

    val siteBuildingLiveData = MutableLiveData<List<ClassInfoBean>>()
    val siteTableLiveData = MutableLiveData<SiteTableRsp.DataBean?>()

    /**
     * 查询场地建筑物列表
     */
    fun getBuildings(){
        apiStores.buildings().enqueue(object :Callback<BaseResponse<List<ClassInfoBean>>>{
            override fun onResponse(
                call: Call<BaseResponse<List<ClassInfoBean>>>,
                response: Response<BaseResponse<List<ClassInfoBean>>>
            ) {
                val body = response.body()
                val data = body?.data
                if (body != null && body.code == 0 && data != null) {
                    siteBuildingLiveData.postValue(data!!)
                    return
                }
                siteBuildingLiveData.postValue(emptyList())
            }

            override fun onFailure(call: Call<BaseResponse<List<ClassInfoBean>>>, t: Throwable) {
                siteBuildingLiveData.postValue(emptyList())
            }
        })
    }

    /**
     * 查询场地课表数据
     * @param type "type": "1", 类型:1班级 2场地
     * @param typeId "typeId": "1491675357620822017", 班级(场地)ID
     * @param weekTime "weekTime": "1" 第几周
     */
    fun getSites(type:String?,typeId:String?, weekTime: String? = null){
        if (TextUtils.isEmpty(typeId)){
            loge("场地id为空，不能查询场地课表数据")
            return
        }
        val param = mutableMapOf<String, Any?>()
        param["type"] = type
        param["typeId"] = typeId
        param["weekTime"] = weekTime
        val toJSONString = JSON.toJSONString(param)
        loge("查询场地课表数据:$toJSONString")
        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        apiStores.sites(requestBody).enqueue(object :Callback<SiteTableRsp>{
            override fun onResponse(call: Call<SiteTableRsp>, response: Response<SiteTableRsp>) {
                val body = response.body()
                val data = body?.data
                if (body != null && body.code == 0 && data != null) {
                    siteTableLiveData.postValue(data)
                    return
                }
                siteTableLiveData.postValue(null)
            }

            override fun onFailure(call: Call<SiteTableRsp>, t: Throwable) {
                siteTableLiveData.postValue(null)
            }

        })
    }
}