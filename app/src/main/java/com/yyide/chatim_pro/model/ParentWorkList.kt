package com.yyide.chatim_pro.model

data class ParentWorkList(
        val list: List<DD>,
        val pageNo: Int,
        val pageSize: Int,
        val total: Int
)
{

data class DD(
    val completion: Int,
    val difficulty: Int,
    val id: String,
    val isFeedback: Boolean,
    val releaseTime: String,
    val subjectName: String,
    val title: String
)
}
