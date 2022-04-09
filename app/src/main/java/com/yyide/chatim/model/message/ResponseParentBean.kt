package com.yyide.chatim.model.message


data class ResponseParentBean(
    val children: List<ChildrenItem>?,
    val name: String = "",
    val id: String = ""
)