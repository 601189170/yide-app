package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/11 18:39
 * @description 描述
 */
data class Repetition(
    var title: String,
    var checked: Boolean
) {
    companion object {
        fun getList(): List<Repetition> {
            val remindList = mutableListOf<Repetition>()
            remindList.add(Repetition("不重复", false))
            remindList.add(Repetition("每个工作日（周一到周五）", false))
            remindList.add(Repetition("每天", false))
            remindList.add(Repetition("每周", false))
            remindList.add(Repetition("每两周", false))
            remindList.add(Repetition("每月", false))
            remindList.add(Repetition("每年", false))
            return remindList
        }
    }
}