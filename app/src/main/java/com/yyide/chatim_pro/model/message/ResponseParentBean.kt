package com.yyide.chatim_pro.model.message


data class ResponseParentBean(
    val children: List<ChildrenItem>?,
    val name: String = "",
    val id: String = ""
)