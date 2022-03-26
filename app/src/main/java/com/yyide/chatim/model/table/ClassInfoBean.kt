package com.yyide.chatim.model.table

data class ClassInfoBean(val children: List<ChildrenItem>,
                         val name: String = "",
                         val pid: Int = 0,
                         val id: String = "")