package com.yyide.chatim.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.message.MessagePushNetWork
import com.yyide.chatim.model.message.AcceptMessageBean
import com.yyide.chatim.model.message.HomeNoticeDetaiBean
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/9 10:40
 * @description HomeFragment的ViewModel
 */
class HomeViewModel : ViewModel() {

    private val acceptMessageLiveData = MutableLiveData<AcceptMessageBean>()
    val acceptMessage = acceptMessageLiveData

    private val confirmLiveData = MutableLiveData<Boolean>()
    val confirmInfo = confirmLiveData

    private val dialogInfoLiveData = MutableLiveData<HomeNoticeDetaiBean>()
    val dialogInfo = dialogInfoLiveData

    /**
     * 查询通知公告收到消息列表
     */
    fun requestAcceptMessage() {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["pageNo"] = 1
            map["pageSize"] = 5
            map["identifier"] = "announcement"
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val data = MessagePushNetWork.requestAcceptMessageList(body)
            if (data.isSuccess) {
                acceptMessageLiveData.value = data.getOrNull()
            } else {
                acceptMessageLiveData.value = AcceptMessageBean()
            }
        }
    }

    /**
     * 请求首页弹窗
     */
    fun showDialogMessage(){
        viewModelScope.launch {
            val result = MessagePushNetWork.showHomeDialog()
            if (result.isSuccess){
                dialogInfoLiveData.value = result.getOrNull()
            }else{
                dialogInfoLiveData.value = HomeNoticeDetaiBean()
            }
        }
    }

    /**
     * 确认弹窗消息
     * @param rId Int
     */
    fun confirmDetail(rId: Int){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["receiveId"] = rId
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val result = MessagePushNetWork.confirmAcceptMessageInfo(body)
            val data = result.getOrNull() ?: false
            confirmLiveData.value = data
        }
    }
}