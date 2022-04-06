package com.yyide.chatim.model.attendance.teacher

data class SignTimeItem(
    val signResult: String = "",
    val address: String = "",
    val signInOutside: Boolean = false,
    val signTime: String = "",
    val signCount: Int = 0,
    val signType: Int = 0
)