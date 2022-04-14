package com.yyide.chatim_pro.model.gate

/**
 * @author liu tao
 * @date 2021/12/30 15:12
 * @description 获取闸机数据相关权限
 */
data class GateDataPermissionRsp(
    val code: Int = 0,
    val success: Boolean = false,
    val msg: String? = null,
    val data: DataBean? = null
) {
    data class DataBean(
        //0全部 1学生 2教职工 3没有权限
        val permission: String? = null
    )
}