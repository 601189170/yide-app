package com.yyide.chatim_pro.kotlin.network.exception

class GlobalException private constructor(message: String) : RuntimeException(message) {
    companion object {
        @JvmStatic
        fun of(message: String) = GlobalException(message)
    }
}