package com.yyide.chatim_pro.model

import com.alibaba.fastjson.annotation.JSONField

data class SubjectBean(
        @JSONField(name = "id")
        val id: String,
        @JSONField(name = "name")
        val name: String,
        @JSONField(name = "isDelete")
        val isDelete: String,

        var checked: Boolean = false


)


