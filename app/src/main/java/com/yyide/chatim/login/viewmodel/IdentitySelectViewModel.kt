package com.yyide.chatim.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.SchoolRsp
import com.yyide.chatim.model.UserBean
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class IdentitySelectViewModel : ViewModel() {
    val schoolLiveData = MutableLiveData<Result<List<SchoolRsp>>>()
    val loginLiveData = MutableLiveData<Result<UserBean>>()

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
            loginLiveData.value = NetworkApi.schoolIdentityLogin(body)
        }
    }
}