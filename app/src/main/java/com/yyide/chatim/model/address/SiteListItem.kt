package com.yyide.chatim.model.address

import com.baozi.treerecyclerview.annotation.TreeDataType
import com.google.gson.annotations.SerializedName
import com.yyide.chatim.adapter.address.AddressItem

@TreeDataType(iClass = AddressItem::class)
data class SiteListItem(@SerializedName("versionStamp")
                        val versionStamp: Int = 0,
                        @SerializedName("classesId")
                        val classesId: String = "",
                        @SerializedName("roomNum")
                        val roomNum: String = "",
                        @SerializedName("schoolId")
                        val schoolId: String = "",
                        @SerializedName("name")
                        val name: String = "",
                        @SerializedName("peppleNum")
                        val peppleNum: Int = 0,
                        @SerializedName("id")
                        val id: String = "",
                        @SerializedName("floor")
                        val floor: Int = 0,
                        @SerializedName("siteTypeId")
                        val siteTypeId: String = "",
                        @SerializedName("space")
                        val space: Double = 0.0,
                        @SerializedName("buildingId")
                        val buildingId: String = "",
                        // 是否选中
                        var check : Boolean = false
)