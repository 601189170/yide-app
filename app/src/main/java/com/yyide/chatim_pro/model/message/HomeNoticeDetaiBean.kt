package com.yyide.chatim_pro.model.message

data class HomeNoticeDetaiBean(val isNeedConfirm: Boolean = false,
                               val isConfirm: Boolean = false,
                               val isView: Boolean = false,
                               val id: Int = 0,
                               val title: String = "",
                               val contentType: Int = 0,
                               val content: String = "",
                               val receiveId: Int = 0,
                               val contentImg: String = "")