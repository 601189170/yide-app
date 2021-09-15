package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/15 14:12
 * @description 描述
 */
data class WeekBean(
    var title:String,
    var checked:Boolean
){
    companion object{
        fun getList():List<WeekBean>{
            val weekList = mutableListOf<WeekBean>()
            weekList.add(WeekBean("周 一",false))
            weekList.add(WeekBean("周 二",false))
            weekList.add(WeekBean("周 三",false))
            weekList.add(WeekBean("周 四",false))
            weekList.add(WeekBean("周 五",false))
            weekList.add(WeekBean("周 六",false))
            weekList.add(WeekBean("周 日",false))
            return weekList
        }

    }
}