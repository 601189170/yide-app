package com.yyide.chatim_pro.activity.message.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.kotlin.network.message.MessagePushNetWork
import com.yyide.chatim_pro.model.message.AcceptMessageBean
import com.yyide.chatim_pro.model.table.ChildrenItem
import com.yyide.chatim_pro.utils.DateUtils
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 15:36
 * @description 描述
 */
class LostFoundViewModel : ViewModel() {

    val noticeTypeByReceive = "0"
    val noticeTypeByPublish = "1"

    val timeByToday = "0"
    val timeByWeek = "1"
    val timeByMonth = "2"

    val mContentList = mutableListOf<ChildrenItem>()
    val mContentTimeList = mutableListOf<ChildrenItem>()

    var selectContent: ChildrenItem? = null
    var selectContentTime: ChildrenItem? = null

    var isTeacher = SpData.getIdentityInfo().staffIdentity()

    private val acceptMessageLiveData = MutableLiveData<AcceptMessageBean>()
    val acceptMessage = acceptMessageLiveData

    /**
     * 查询失物招领收到消息列表
     * @param pageNo Int
     * @param pageSize Int
     */
    fun requestAcceptMessage(pageNo: Int, pageSize: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["pageNo"] = pageNo
            map["pageSize"] = pageSize
            map["identifier"] = "receive"
            map["startTime"] = getStartTime()
            map["endTime"] = getEndTime()
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val data = MessagePushNetWork.requestAcceptMessageList(body)
            if (data.isSuccess) {
                acceptMessageLiveData.value = data.getOrNull()
            }else{
                acceptMessageLiveData.value = AcceptMessageBean()
            }
        }
    }

    /**
     * 查询失物招领发布消息列表
     * @param pageNo Int
     * @param pageSize Int
     */
    fun requestPublishMessage(pageNo: Int, pageSize: Int) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["pageNo"] = pageNo
            map["pageSize"] = pageSize
            map["identifier"] = "receive"
            map["startTime"] = getStartTime()
            map["endTime"] = getEndTime()
            val body = JSON.toJSONString(map).toRequestBody(BaseConstant.JSON)
            val data = MessagePushNetWork.requestPublishMessageList(body)
            if (data.isSuccess) {
                acceptMessageLiveData.value = data.getOrNull()
            } else {
                acceptMessageLiveData.value = AcceptMessageBean()
            }
        }
    }

    private fun getStartTime():String{
        var starTime = DateUtils.getDayBegin()
        when (selectContentTime?.id) {
            timeByWeek -> {
                starTime = DateUtils.getDate(DateUtils.getBeginDayOfWeek().time)
            }
            timeByMonth -> {
                starTime = DateUtils.getDate(DateUtils.getBeginDayOfMonth().time)
            }
        }
        return starTime
    }

    private fun getEndTime():String{
        var endTime = DateUtils.getDayEndDate(System.currentTimeMillis())
        when (selectContentTime?.id) {
            timeByWeek -> {
                endTime = DateUtils.getDate(DateUtils.getEndDayOfWeek().time)
            }
            timeByMonth -> {
                endTime = DateUtils.getDate(DateUtils.getEndDayOfMonth().time)
            }
        }
        return endTime
    }
}