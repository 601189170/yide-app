package com.yyide.chatim_pro.adapter.table

import com.contrarywind.adapter.WheelAdapter

/**
 *
 * @author shenzhibin
 * @date 2022/3/29 19:16
 * @description 滚动选择器的适配器
 */
class TableClassWheelAdapter(listData: List<String>) : WheelAdapter<String> {

    val data = mutableListOf<String>()

    init {
        this.data.clear()
        this.data.addAll(listData)
    }


    override fun getItemsCount(): Int {
        return data.size
    }

    override fun getItem(index: Int): String {
        return data[index]
    }

    override fun indexOf(o: String): Int {
        return data.indexOf(o)
    }
}