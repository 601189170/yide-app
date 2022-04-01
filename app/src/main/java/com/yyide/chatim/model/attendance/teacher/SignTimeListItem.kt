package com.yyide.chatim.model.attendance.teacher

data class SignTimeListItem(var signResult: String = "",
                            var shouldSignTime: String = "",
                            var signCount: Int = 0,
                            var signType: Int = 0,
                            var actualSignTime: String = "")