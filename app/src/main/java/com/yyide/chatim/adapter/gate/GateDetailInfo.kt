package com.yyide.chatim.adapter.gate

/**
 *
 * @author liu tao
 * @date 2021/12/21 16:37
 * @description 描述
 */
data class GateDetailInfo(
    val date:String,
    val time:String,
    val type:Int,
    val title:String
)

/**
 * 通行数据
 */
data class GateThroughData(
    val type: Int,//类型 3列 4列 2列
    val name:String,
    val time: String,
    val clazz:String,
    val address:String
)

/**
 * 通行列表部门数据
 * 出校 入校
 */
data class GateBranchData(
    val branch:String,
    val total:Int,
    val out:Int,
    val into:Int
)