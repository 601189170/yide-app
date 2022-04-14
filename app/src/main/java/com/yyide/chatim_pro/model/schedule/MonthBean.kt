package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/15 14:12
 * @description 描述
 */
data class MonthBean(
    var title:String,
    var checked:Boolean
){
    companion object{
        fun getList():List<MonthBean>{
            val weekList = mutableListOf<MonthBean>()
            for (index in 1..31){
                weekList.add(MonthBean("$index",false))
            }
            return weekList
        }

    }
}