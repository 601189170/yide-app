package com.yyide.chatim.activity.weekly.home

import com.tencent.mmkv.MMKV
import com.yyide.chatim.base.MMKVConstant

object WeeklyUtil {

    fun getDesc(): String {
        val decodeStringSet = MMKV.defaultMMKV().decodeStringSet(MMKVConstant.YD_WEEKLY_DESC)
        val list = mutableListOf<String>()
        var desc = ""
        list.addAll(decodeStringSet)
        if (list.size > 0) {
            val random = (0 until list.size).random()
            desc = list[random]
        }
        return desc
    }
}