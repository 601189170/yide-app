package com.yyide.chatim_pro.net

/**
 *
 * @author liu tao
 * @date 2021/12/30 17:32
 * @description 闸机数据
 */
object GateDataService {
    val gateDataApi = AppClient.getDingRetrofit().create(GateDataApi::class.java)
}