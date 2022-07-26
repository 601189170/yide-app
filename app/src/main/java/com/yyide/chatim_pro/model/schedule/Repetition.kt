package com.yyide.chatim_pro.model.schedule

import com.alibaba.fastjson.JSON

/**
 *
 * @author liu tao
 * @date 2021/9/11 18:39
 * @description 描述
 */
data class Repetition(
    var code:Int=0,
    var title: String? = null,
    var checked: Boolean = false,
    var rule:MutableMap<String,Any>? = null
) {
    companion object {
        fun getNotRepetition(): Repetition {
            return Repetition(0,"不重复", true,null)
        }
        fun getList(): List<Repetition> {
            val remindList = mutableListOf<Repetition>()
            remindList.add(Repetition(0,"不重复", true,null))
            //{"freq": "weekly","interval": "1","byday":"MO,TU,WE,TH,FR"}
            val rule1 = mutableMapOf<String,Any>()
            rule1["freq"] = "weekly"
            rule1["interval"] = "1"
            rule1["byweekday"] = JSON.toJSONString(listOf("MO","TU","WE","TH","FR"))
            remindList.add(Repetition(5,"每个工作日（周一到周五）", false,rule1))
            //{"freq": "daily","interval": "1"}
            val rule2 = mutableMapOf<String,Any>()
            rule2["freq"] = "daily"
            rule2["interval"] = "1"
            remindList.add(Repetition(1,"每天", false,rule2))
            //{"freq": "weekly","interval": "1"}
            val rule3 = mutableMapOf<String,Any>()
            rule3["freq"] = "weekly"
            rule3["interval"] = "1"
            remindList.add(Repetition(2,"每周", false,rule3))
            //{"freq": "weekly","interval": "2"}
            val rule4 = mutableMapOf<String,Any>()
            rule4["freq"] = "weekly"
            rule4["interval"] = "2"
            remindList.add(Repetition(6,"每两周", false,rule4))
            //{"freq": "monthly","interval": "1"}
            val rule5 = mutableMapOf<String,Any>()
            rule5["freq"] = "monthly"
            rule5["interval"] = "1"
            remindList.add(Repetition(3,"每月", false,rule5))
            //{"freq": "yearly","interval": "1"}
            val rule6 = mutableMapOf<String,Any>()
            rule6["freq"] = "yearly"
            rule6["interval"] = "1"
            remindList.add(Repetition(4,"每年", false,rule6))
            return remindList
        }
    }
}