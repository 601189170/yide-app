package com.yyide.chatim.kotlin.network.base

/**
 * 网络数据返回基类
 */
data class BaseResponse<T>(
    var code: Int = 0,
    val msg: String? = null,
    val redirect: String? = null,
    val data: T? = null
)