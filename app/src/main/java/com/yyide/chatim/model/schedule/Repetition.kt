package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/11 18:39
 * @description 描述
 */
data class Repetition(
    var title: String,
    var checked: Boolean,
    var rule:String
) {
    companion object {
        fun getList(): List<Repetition> {
            val remindList = mutableListOf<Repetition>()
            remindList.add(Repetition("不重复", true,""))
            remindList.add(Repetition("每个工作日（周一到周五）", false,"{\"freq\": \"weekly\",\"interval\": \"1\",\"byday\":\"MO,TU,WE,TH,FR\"}"))
            remindList.add(Repetition("每天", false,"{\"freq\": \"daily\",\"interval\": \"1\"}"))
            remindList.add(Repetition("每周", false,"{\"freq\": \"weekly\",\"interval\": \"1\"}"))
            remindList.add(Repetition("每两周", false,"{\"freq\": \"weekly\",\"interval\": \"2\"}"))
            remindList.add(Repetition("每月", false,"{\"freq\": \"monthly\",\"interval\": \"1\"}"))
            remindList.add(Repetition("每年", false,"{\"freq\": \"yearly\",\"interval\": \"1\"}"))
            return remindList
        }
    }
}