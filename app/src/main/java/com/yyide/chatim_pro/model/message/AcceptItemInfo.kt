package com.yyide.chatim_pro.model.message

data class AcceptItemInfo(
    val viewUsers: Int = 0,
    val timerDate: String = "",
    val id: Int = 0,
    val source: String = "",
    val confirmUsers: Int = 0,
    val identityUserName: String = "",
    val title: String = "",
    val contentType: Int = 0,
    val content: String = "",
    val contentImg: String = "",
    var isNeedConfirm: Boolean = false
)