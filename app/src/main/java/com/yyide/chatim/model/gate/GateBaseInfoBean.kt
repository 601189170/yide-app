package com.yyide.chatim.model.gate

/**
 * @author liu tao
 * @date 2022/1/5 13:43
 * @description 描述
 */
data class GateBaseInfoBean(
    var totalNumber: Int = 0,
    var outNumber: Int = 0,
    var intoNumber: Int = 0,
    var name: String? = null,
    var id: String? = null,
    var type: String? = null,
    var userId:String?=null,
    var isDept:Boolean = false
)

data class BarrierSectionBean(
    var id:String,
    var name: String?,
    val list:List<GateBaseInfoBean>
)

