package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable

data class SchoolRsp(
    @JSONField(name = "children")
    var children: List<IdentityBean> = mutableListOf(),
    @JSONField(name = "enabled")
    var enabled: Boolean = false,
    @JSONField(name = "id")
    var id: Long = 0,
    @JSONField(name = "init")
    var `init`: Boolean = false,
    @JSONField(name = "schoolLogo")
    var schoolLogo: String = "",
    @JSONField(name = "schoolName")
    var schoolName: String = ""
)

data class IdentityBean(
    @JSONField(name = "enabled")
    var enabled: Boolean = false,
    @JSONField(name = "id")
    var id: Long = 0,
    @JSONField(name = "identity")
    var identity: String = "",
    @JSONField(name = "identityName")
    var identityName: String = ""
) {
    companion object {
        val IDENTITY_TEACHER = "1"
        val IDENTITY_PARENTS = "2"
    }
}

data class UserBean(
    @JSONField(name = "avatar")
    var avatar: String,
    @JSONField(name = "email")
    var email: String,
    @JSONField(name = "gender")
    var gender: Int,
    @JSONField(name = "id")
    var id: String,
    @JSONField(name = "identity")
    var identity: Int,
    @JSONField(name = "identitys")
    var identitys: List<IdentityBean>,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "phone")
    var phone: String,
    @JSONField(name = "username")
    var username: String
) : Serializable




