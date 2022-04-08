package com.yyide.chatim.model.message

import com.google.gson.annotations.SerializedName

data class AcceptMessageBean(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("pageNo")
    val pageNo: Int = 0,
    @SerializedName("pageSize")
    val pageSize: Int = 0,
    @SerializedName("list")
    val acceptMessage: List<AcceptMessageItem> = mutableListOf()
)