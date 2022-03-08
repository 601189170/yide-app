package com.yyide.chatim.kotlin.network.base

/**
 * 网络数据返回基类
 */
open class BaseResponse<T>(
    val code: Int = 0,
    val message: String? = null,
    val redirect: String? = null,
    val data: T? = null
){
}
