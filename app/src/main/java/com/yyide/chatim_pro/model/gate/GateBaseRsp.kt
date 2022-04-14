package com.yyide.chatim_pro.model.gate

/**
 *
 * @author liu tao
 * @date 2021/12/30 17:23
 * @description 描述
 */
data class GateBaseRsp<T>(
    var code: Int = 0,
    var isSuccess: Boolean = false,
    var msg: String? = null,
    var data: T? = null
)