package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField


data class FaceStudentBean(
    @JSONField(name = "className")
    var className: String = "",
    @JSONField(name = "gender")
    var gender: String = "",
    @JSONField(name = "id")
    var id: String = "",
    @JSONField(name = "isGather")
    var isGather: Boolean = false,
    @JSONField(name = "name")
    var name: String = ""
)