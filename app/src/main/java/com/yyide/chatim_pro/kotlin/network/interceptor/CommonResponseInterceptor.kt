package com.yyide.chatim_pro.kotlin.network.interceptor

import android.content.Intent
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.yyide.chatim_pro.BaseApplication
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.alipush.AliasUtil
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.ErrorCode
import com.yyide.chatim_pro.kotlin.network.base.BaseNetworkApi
import com.yyide.chatim_pro.kotlin.network.base.BaseResponse
import com.yyide.chatim_pro.login.NewLoginActivity
import com.yyide.chatim_pro.utils.logd
import okhttp3.Interceptor
import okhttp3.Response
import okio.BufferedSource
import java.nio.charset.Charset

class CommonResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis()
        val request = chain.request()
        val response = chain.proceed(request)
        Log.d(BaseNetworkApi.TAG, "url=${request.url}, requestTime=${System.currentTimeMillis() - startTime}ms")
        logd("status code = ${response.code}")
        if (isTokenExpired(response)) {
            //清空消息推送别名
            SPUtils.getInstance().remove(BaseConstant.PASSWORD)
            SPUtils.getInstance().remove(BaseConstant.JG_ALIAS_NAME)
            SPUtils.getInstance().remove(SpData.LOGINDATA)
            SPUtils.getInstance().remove(SpData.IDENTIY_INFO)
            AliasUtil.clearAlias()
            //退出登录IM
            TUIKit.logout(object : IUIKitCallBack {
                override fun onSuccess(data: Any) {
                }

                override fun onError(module: String, errCode: Int, errMsg: String) {
                }
            })
            BaseApplication.getInstance().startActivity(
                Intent(
                    BaseApplication.getInstance(),
                    NewLoginActivity::class.java
                )
            )
        }
        return response
    }

    private fun isTokenExpired(response: Response):Boolean{

        if (response.code == ErrorCode.UNAUTHORIZED){
            return true
        }

        // 判断服务器返回的code是不是401
        val responseBody = response.body
        var jsonStr = ""
        responseBody?.let {
            val source = it.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer
            val utf8Format = Charset.forName("UTF-8")
            jsonStr = buffer.clone().readString(utf8Format)
        }

        if (jsonStr.isNotEmpty()){
            val rsp = GsonUtils.fromJson(jsonStr,BaseResponse::class.java)
            if (rsp.code == ErrorCode.UNAUTHORIZED){
                return true
            }
        }

        return false

    }

}