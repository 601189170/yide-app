package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField


data class SchoolRsp(
    val id: Long = 0,
    val isInit: Boolean = false,
    val schoolLogo: String = "",
    val schoolName: String = "",
    val isEnabled: Boolean = false,
    val children: List<IdentityBean> = mutableListOf<IdentityBean>(),
) {
    //    children	array	身份信息
//    children.id	string	身份ID
//    children.identity	string	身份
//    children.identityName	string	身份名称
//    id	string	学校ID
//    isInit	string	是否初始化
//    schoolLogo	string	学校Logo
//    schoolName	string	学校名称
    data class IdentityBean(
        val id: Long = 0,
        val identity: String = "",
        val identityName: String = "",
        val isEnabled: Boolean = false,
    )
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
    var identitys: List<Identity>,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "phone")
    var phone: String,
    @JSONField(name = "username")
    var username: String
)

data class Identity(
    @JSONField(name = "id")
    var id: String,
    @JSONField(name = "identity")
    var identity: Int,
    @JSONField(name = "identityName")
    var identityName: String,
    @JSONField(name = "isEnabled")
    var isEnabled: Boolean
)


