package com.yyide.chatim_pro.model.attendance.teacher

data class SignTimeListItem(val signTime: Int = 0,
                            val signCount: Int = 0,
                            val signType: Int = 0,
                            val needSign: Boolean = false,
                            var signResult: String = "",
                            var shouldSignTime: String = "",
                            var actualSignTime: String = "")