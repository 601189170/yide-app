package com.yyide.chatim_pro.model.gate

/**
 * @author liu tao
 * @date 2022/1/6 19:39
 * @description 教职工列表数据
 */
data class StaffBean(
    var totalNumber: Int = 0,
    var outNumber: Int = 0,
    var intoNumber: Int = 0,
    var name: String? = null,
    var id: String? = null,
    var deptDataList: List<GateBaseInfoBean>? = null,
    var list: List<GateBaseInfoBean>? = null
)