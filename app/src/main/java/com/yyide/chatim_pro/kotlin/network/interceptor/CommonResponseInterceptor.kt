package com.yyide.chatim_pro.kotlin.network.interceptor

import android.util.Log
import com.yyide.chatim_pro.kotlin.network.base.BaseNetworkApi
import okhttp3.Interceptor
import okhttp3.Response

class CommonResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis()
        val request = chain.request()
        val response = chain.proceed(request)
        Log.d(BaseNetworkApi.TAG, "url=${request.url}, requestTime=${System.currentTimeMillis() - startTime}ms")
        return response
    }
}