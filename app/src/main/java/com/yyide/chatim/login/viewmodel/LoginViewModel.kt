package com.yyide.chatim.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yyide.chatim.base.BaseViewModel
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.LoginRsp
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

class LoginViewModel : BaseViewModel() {
    val loginLiveData = MutableLiveData<Result<LoginRsp>>()

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

}