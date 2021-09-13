package com.yyide.chatim.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/11 18:39
 * @description 描述
 */
data class Address(
    var title: String,
    var checked: Boolean
) {
    companion object {
        fun getList(): List<Address> {
            val remindList = mutableListOf<Address>()
            remindList.add(Address("物理实验室", false))
            remindList.add(Address("阶梯教室一", false))
            remindList.add(Address("化学实验室", false))
            remindList.add(Address("十五栋301", false))
            remindList.add(Address("十五栋302", false))
            remindList.add(Address("十五栋303", false))
            remindList.add(Address("十五栋304", false))
            remindList.add(Address("十三栋305", false))
            remindList.add(Address("十三栋306", false))
            remindList.add(Address("十三栋307", false))
            return remindList
        }
    }
}