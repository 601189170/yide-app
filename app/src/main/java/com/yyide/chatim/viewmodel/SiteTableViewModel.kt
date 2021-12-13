package com.yyide.chatim.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.sitetable.SiteBuildingsRsp
import com.yyide.chatim.model.sitetable.SiteTableRsp
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
 * @date 2021/12/9 11:26
 * @description 描述
 */
class SiteTableViewModel : ViewModel() {
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)
    val siteBuildingLiveData = MutableLiveData<SiteBuildingsRsp.DataBean>()
    val siteTableLiveData = MutableLiveData<SiteTableRsp.DataBean?>()

    /**
     * 查询场地建筑物列表
     */
    fun getBuildings(){
        apiStores.buildings().enqueue(object :Callback<SiteBuildingsRsp>{
            override fun onResponse(
                call: Call<SiteBuildingsRsp>,
                response: Response<SiteBuildingsRsp>
            ) {
                val body = response.body()
                val data = body?.data
                if (body != null && body.code == 200 && data != null) {
                    siteBuildingLiveData.postValue(data!!)
                    return
                }
                val dataBean = SiteBuildingsRsp.DataBean()
                dataBean.isExistsSiteKcb = false
                siteBuildingLiveData.postValue(dataBean)
            }

            override fun onFailure(call: Call<SiteBuildingsRsp>, t: Throwable) {
                val dataBean = SiteBuildingsRsp.DataBean()
                dataBean.isExistsSiteKcb = false
                siteBuildingLiveData.postValue(dataBean)
            }
        })
    }

    /**
     * 查询场地课表数据
     */
    fun getSites(cdid:String?,skzc:String?){
        if (TextUtils.isEmpty(cdid)){
            loge("场地id为空，不能查询场地课表数据")
            return
        }
        val param = mutableMapOf<String, Any?>()
        param.put("cdid",cdid)
        param.put("skzc",skzc)
        val toJSONString = JSON.toJSONString(param)
        loge("查询场地课表数据:$toJSONString")
        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        apiStores.sites(requestBody).enqueue(object :Callback<SiteTableRsp>{
            override fun onResponse(call: Call<SiteTableRsp>, response: Response<SiteTableRsp>) {
                val body = response.body()
                val data = body?.data
                if (body != null && body.code == 200 && data != null) {
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