package com.yyide.chatim.model.address


import com.baozi.treerecyclerview.annotation.TreeDataType
import com.google.gson.annotations.SerializedName
import com.yyide.chatim.adapter.address.AddressItemParent

@TreeDataType(iClass = AddressItemParent::class)
data class ScheduleAddressBean(@SerializedName("versionStamp")
                               val versionStamp: Int = 0,
                               @SerializedName("name")
                               val name: String = "",
                               @SerializedName("id")
                               val id: String = "",
                               @SerializedName("siteList")
                               val siteList: List<SiteListItem>){


    constructor():this(0,"","", emptyList())

}