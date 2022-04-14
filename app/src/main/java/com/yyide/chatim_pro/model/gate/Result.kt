package com.yyide.chatim_pro.model.gate

sealed class Result<out T> {
    //成功
    data class Success<T>(var data: T?) : Result<T>()

    //失败
    data class Error<T>(var exception: Throwable?) : Result<T>()
}