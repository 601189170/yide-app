package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/11 18:39
 * @description 描述
 */
data class Remind(
    var title: String,
    var checked: Boolean
) {
    companion object {
        fun getList(): List<Remind> {
            val remindList = mutableListOf<Remind>()
            remindList.add(Remind("日程开始时", false))
            remindList.add(Remind("5分钟前", false))
            remindList.add(Remind("10分钟前", false))
            remindList.add(Remind("15分钟前", false))
            remindList.add(Remind("30分钟前", false))
            remindList.add(Remind("1小时前", false))
            remindList.add(Remind("2小时前", false))
            remindList.add(Remind("1天前", false))
            remindList.add(Remind("2天前", false))
            remindList.add(Remind("1周前", false))
            return remindList
        }
    }
}