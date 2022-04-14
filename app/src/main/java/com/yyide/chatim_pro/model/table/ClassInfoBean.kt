package com.yyide.chatim_pro.model.table

data class ClassInfoBean(val children: List<ChildrenItem>,
                         val name: String = "",
                         val pid: Int = 0,
                         val id: String = "")