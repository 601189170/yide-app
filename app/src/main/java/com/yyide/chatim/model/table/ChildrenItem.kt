package com.yyide.chatim.model.table

data class ChildrenItem(
    var name: String = "",
    val pid: String = "",
    val id: String = "",
    var parentName: String = ""
){

    constructor(name: String,pid: String,id: String):this(name, pid, id,"")

}