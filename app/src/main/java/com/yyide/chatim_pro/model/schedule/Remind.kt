package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/11 18:39
 * @description
 * 提醒周期【0：5分钟前、1：10分钟前、2：15分钟前、3：30分钟前、4：1小时前、5：2小时前、6：1天前、7：2天前、8：1周前、9：日程开始时】
 */
data class Remind(
    var id: String? = null,
    var title: String? = null,
    var checked: Boolean = false
) {
    companion object {
        fun getNotRemind(): Remind {
            return Remind("10", "不提醒", false)
        }

        fun getList(): List<Remind> {
            val remindList = mutableListOf<Remind>()
            remindList.add(Remind("9", "日程开始时", false))
            remindList.add(Remind("0", "5分钟前", false))
            remindList.add(Remind("1", "10分钟前", false))
            remindList.add(Remind("2", "15分钟前", false))
            remindList.add(Remind("3", "30分钟前", false))
            remindList.add(Remind("4", "1小时前", false))
            remindList.add(Remind("5", "2小时前", false))
            remindList.add(Remind("6", "1天前", false))
            remindList.add(Remind("7", "2天前", false))
            remindList.add(Remind("8", "1周前", false))
            return remindList
        }

        fun getList2(): List<Remind> {
            val remindList = mutableListOf<Remind>()
            remindList.add(Remind("11", "当天07:00", false))
            remindList.add(Remind("12", "当天08:00", false))
            remindList.add(Remind("13", "当天09:00", false))
            remindList.add(Remind("14", "提前一天07:00", false))
            remindList.add(Remind("15", "提前一天08:00", false))
            remindList.add(Remind("16", "提前一天09:00", false))
            return remindList
        }
    }
}