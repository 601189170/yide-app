package com.yyide.chatim_pro.viewmodel

import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.yyide.chatim_pro.net.GateDataService
import java.lang.RuntimeException

/**
 *
 * @author liu tao
 * @date 2022/1/6 14:09
 * @description 描述
 */
open class BaseViewModel : ViewModel() {
    protected fun networkExceptionHandler() {
        if (!NetworkUtils.isConnected()) throw RuntimeException("网络未连接")
    }
    protected val gateDataApi = GateDataService.gateDataApi
}