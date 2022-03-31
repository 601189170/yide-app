package com.yyide.chatim.model.attendance.teacher

data class SignTimeListItem(val signResult: String = "",
                            val shouldSignTime: String = "",
                            val signCount: Int = 0,
                            val signType: Int = 0,
                            val actualSignTime: String = "")