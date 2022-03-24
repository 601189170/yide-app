package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.address.ScheduleAddressBean
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.loge
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/9/23 9:40
 * @description 描述
 */
class SiteManageViewModel : ViewModel() {
    private var dingApiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)

    private val siteInfoListLiveData = MutableLiveData<List<ScheduleAddressBean>>()
    val siteInfoListData: LiveData<List<ScheduleAddressBean>> get() = siteInfoListLiveData

    val selectAddress:SiteNameRsp.DataBean = SiteNameRsp.DataBean("", "无场地", true)
    val selectFirstAddress : ScheduleAddressBean = ScheduleAddressBean()
    val selectSecondIndex = 0

    /**
     * 查询场地信息
     */
    fun getSiteName() {
        dingApiStores.siteName.enqueue(object : Callback<BaseResponse<List<ScheduleAddressBean>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<ScheduleAddressBean>>>,
                response: Response<BaseResponse<List<ScheduleAddressBean>>>
            ) {
                val body = response.body()
                logd("code = ${body?.code}")
                if (body != null) {
                    siteInfoListLiveData.value = body.data ?: listOf()
                    return
                }
                siteInfoListLiveData.value = listOf()
            }

            override fun onFailure(
                call: Call<BaseResponse<List<ScheduleAddressBean>>>,
                t: Throwable
            ) {
                logd("error = ${t.message}")
                siteInfoListLiveData.value =listOf()
            }
        })
    }
}