package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/15 14:12
 * @description 描述
 */
data class WeekBean(
    var title:String,
    var shortname:String,
    var checked:Boolean
){
    companion object{
        fun getList():List<WeekBean>{
            val weekList = mutableListOf<WeekBean>()
            //MO(周一),TU(周二),WE(周三),TH(周四),FR(周五),SA(周六),SU(周日)
            weekList.add(WeekBean("周 一","MO",false))
            weekList.add(WeekBean("周 二","TU",false))
            weekList.add(WeekBean("周 三","WE",false))
            weekList.add(WeekBean("周 四","TH",false))
            weekList.add(WeekBean("周 五","FR",false))
            weekList.add(WeekBean("周 六","SA",false))
            weekList.add(WeekBean("周 日","SU",false))
            return weekList
        }

    }
}