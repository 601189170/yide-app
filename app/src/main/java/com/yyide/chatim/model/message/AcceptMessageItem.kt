package com.yyide.chatim.model.message

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class AcceptMessageItem(
    @SerializedName("viewUsers")
    val viewUsers: Int = 0,
    @SerializedName("isConfirm")
    var isConfirm: Boolean = false,
    @SerializedName("isView")
    val isView: Boolean = false,
    @SerializedName("timerDate")
    val timerDate: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("confirmUsers")
    val confirmUsers: Int = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("contentType")
    val contentType: Int = 0,
    @SerializedName("messType")
    val messType: Int = 0,
    @SerializedName("receiveId")
    val receiveId: Int = 0,
    val identityUserName: String = "",
    var isTop: Boolean = false,
    var isRetract: Boolean = false,
    val isTimer: Boolean = false,
    var receiveUsers: String = "",
    var notifyUsersInfo: String = ""
) : Parcelable