package com.yyide.chatim.utils

import android.util.Log

/**
 *
 * @Description: 扩展函数
 * @Author: liu tao
 * @CreateDate: 2021/5/8 14:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/8 14:06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

fun Any.loge(string: String) {
    val tag = javaClass.name.substring(javaClass.name.lastIndexOf(".") + 1)
    Log.e(tag, string)
}

fun Any.logd(string: String) {
    val tag = javaClass.name.substring(javaClass.name.lastIndexOf(".") + 1)
    Log.d(tag, string)
}

fun Any.loge(string: String,flag:Boolean) {
    if (flag){
        val tag = javaClass.name.substring(javaClass.name.lastIndexOf(".") + 1)
        Log.e(tag, string)
    }
}

fun Any.logd(string: String,flag:Boolean){
    if (flag){
        val tag = javaClass.name.substring(javaClass.name.lastIndexOf(".") + 1)
        Log.d(tag, string)
    }
}