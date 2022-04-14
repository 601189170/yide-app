package com.yyide.chatim_pro.model

data class getClassList(
        val children: List<Children>,
        val id: String,
        val name: String,
        val pid: String
) {
    data class Children(
            val children: List<Any>,
            val id: String,
            val name: String,
            val pid: String
    )
}