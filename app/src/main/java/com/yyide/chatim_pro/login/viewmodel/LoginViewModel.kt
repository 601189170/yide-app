package com.yyide.chatim_pro.login.viewmodel

import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.base.BaseViewModel
import com.yyide.chatim_pro.base.SingleLiveEvent
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.LoginRsp
import com.yyide.chatim_pro.model.SchoolRsp
import com.yyide.chatim_pro.model.UserBean
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

class LoginViewModel : BaseViewModel() {
    val loginLiveData = SingleLiveEvent<Result<LoginRsp>>()
    val schoolLiveData = SingleLiveEvent<Result<List<SchoolRsp>>>()
    val identityLoginLiveData = SingleLiveEvent<Result<UserBean>>()

    fun login(userName: String, pwd: String) {
        viewModelScope.launch {
            val body: RequestBody = FormBody.Builder()
                .add("client_id", "yide-cloud")
                .add("grant_type", "password")
                .add("client_secret", "yide1234567")
                .add("username", userName)
                .add("password", pwd)
                .build()
            loginLiveData.value = NetworkApi.login(body)
        }
    }

    /**
     * 获取学校信息
     */
    fun schoolIdentity() {
        viewModelScope.launch {
            schoolLiveData.value = NetworkApi.getSchoolIdentityInfo()
        }
    }

    /**
     * 身份登录
     */
    fun identityLogin(identityId: Long, schoolId: Long) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["id"] = identityId
            map["schoolId"] = schoolId
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            identityLoginLiveData.value = NetworkApi.schoolIdentityLogin(body)
        }
    }

}