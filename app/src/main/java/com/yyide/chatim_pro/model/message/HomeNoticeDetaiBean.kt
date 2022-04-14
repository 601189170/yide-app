package com.yyide.chatim_pro.model.message

data class HomeNoticeDetaiBean(val isNeedConfirm: Boolean = false,
                               val isConfirm: Boolean = false,
                               val id: Int = 0,
                               val contentType: Int = 0,
                               val content: String = "",
                               val contentImg: String = "")