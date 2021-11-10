package com.yyide.chatim.model.schedule

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
            for (i in 1..30) {
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
            for (i in 1..71) {
                listWeek.add("" + i)
            }
            repetitionDataBeanWeek.number = listWeek
            repetitionDataBeanList.add(repetitionDataBeanWeek)

            return repetitionDataBeanList
        }
    }
}