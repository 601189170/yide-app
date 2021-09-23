package com.yyide.chatim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
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
    private var siteInfoList = MutableLiveData<List<SiteNameRsp.DataBean>>()
    private var dingApiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)

    fun getSiteInfoList(): LiveData<List<SiteNameRsp.DataBean>> {
        return siteInfoList
    }

    init {
        getSiteName()
    }

    /**
     * 查询场地信息
     */
    fun getSiteName() {
        dingApiStores.siteName.enqueue(object : Callback<SiteNameRsp> {
            override fun onResponse(call: Call<SiteNameRsp>, response: Response<SiteNameRsp>) {
                loge("getSiteName:${response.body()}")
                val body = response.body()
                if (body != null) {
                    siteInfoList.postValue(body.data ?: listOf())
                    return
                }
                siteInfoList.postValue(listOf())
            }

            override fun onFailure(call: Call<SiteNameRsp>, t: Throwable) {
                siteInfoList.postValue(listOf())
            }
        })
    }
}