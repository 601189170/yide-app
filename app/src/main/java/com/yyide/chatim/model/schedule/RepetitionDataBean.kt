package com.yyide.chatim.model.schedule

import com.yyide.chatim.database.ScheduleDaoUtil

/**
 *
 * @author liu tao
 * @date 2021/11/10 10:59
 * @description 描述
 */
data class RepetitionDataBean(
    var unit: String,
    var number: List<String> = mutableListOf()
) {
    companion object {

        fun getUnitIndex(unit: String, list: List<RepetitionDataBean>): Int {
            for (index in list.indices) {
                if (list[index].unit == unit) {
                    return index
                }
            }
            return 0
        }

        fun getNumberList(unit: String, list: List<RepetitionDataBean>): List<String> {
            list.forEach {
                if (unit == it.unit) {
                    return it.number
                }
            }
            return emptyList()
        }

        fun getList(): List<RepetitionDataBean> {
            val repetitionDataBeanList = mutableListOf<RepetitionDataBean>()
            val repetitionDataBeanDay = RepetitionDataBean("天")
            val listDay = mutableListOf<String>()
            for (i in 1..31) {
                listDay.add("" + i)
            }
            repetitionDataBeanDay.number = listDay
            repetitionDataBeanList.add(repetitionDataBeanDay)

            val repetitionDataBeanMonth = RepetitionDataBean("月")
            val listMonth = mutableListOf<String>()
            for (i in 1..12) {
                listMonth.add("" + i)
            }
            repetitionDataBeanMonth.number = listMonth
            repetitionDataBeanList.add(repetitionDataBeanMonth)

            val repetitionDataBeanWeek = RepetitionDataBean("周")
            val listWeek = mutableListOf<String>()
            for (i in 1..52) {
                listWeek.add("" + i)
            }
            repetitionDataBeanWeek.number = listWeek
            repetitionDataBeanList.add(repetitionDataBeanWeek)

            return repetitionDataBeanList
        }

        fun getYearList(): List<String> {
            val mutableListOf = mutableListOf<String>()
            for (i in 2020..2120) {
                mutableListOf.add("$i")
            }
            return mutableListOf
        }

        fun getMonthList(): List<String> {
            val mutableListOf = mutableListOf<String>()
            for (i in 1..12) {
                mutableListOf.add("$i")
            }
            return mutableListOf
        }

        fun getDayList(year:String,month:String):List<String>{
            val dateTime = ScheduleDaoUtil.toDateTime("$year-$month-01 00:00:00")
            val minimumValue = dateTime.dayOfMonth().minimumValue
            val maximumValue = dateTime.dayOfMonth().maximumValue
            val mutableListOf = mutableListOf<String>()
            for (i in minimumValue .. maximumValue){
                mutableListOf.add("$i")
            }
            return mutableListOf
        }
    }
}