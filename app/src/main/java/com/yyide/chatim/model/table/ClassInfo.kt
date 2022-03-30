package com.yyide.chatim.model.table

/**
 *
 * @author shenzhibin
 * @date 2022/3/29 17:04
 * @description 所选班级的信息
 */
data class ClassInfo(
    var name: String = "",
    var id: String = "",
    var parentId: String = "",
    var parentName: String = ""
){
    override fun toString(): String {
        return "$name,$parentName"
    }
}
