package com.yyide.chatim_pro.viewmodel.gate

import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.model.BaseRsp
import com.yyide.chatim_pro.model.PushSettingBean
import com.yyide.chatim_pro.model.gate.GateBaseRsp
import com.yyide.chatim_pro.model.gate.Result
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.lang.Exception
import java.util.*

/**
 *
 * @author liu tao
 * @date 2022/1/10 17:59
 * @description 推送设置
 */
class PushSettingViewModel : BaseViewModel() {
    lateinit var requestBody: RequestBody
    private val _getPushSettingList =
        MutableStateFlow<Result<List<PushSettingBean>>>(Result.Success(null))
    val getPushSettingList: StateFlow<Result<List<PushSettingBean>>> =
        _getPushSettingList

    private val _updatePushSettingList = MutableStateFlow<Result<BaseRsp>>(Result.Success(null))
    val updatePushSettingList: StateFlow<Result<BaseRsp>> = _updatePushSettingList

    private fun getPushSettingListRequest(): Flow<GateBaseRsp<List<PushSettingBean>>> = flow {
        networkExceptionHandler()
        val queryUserNoticeOnOffByUserId = gateDataApi.queryUserNoticeOnOffByUserId()
        emit(queryUserNoticeOnOffByUserId)
    }

    private fun updatePushSettingListRequest(): Flow<BaseRsp> = flow {
        networkExceptionHandler()
        val updateUserNoticeOnOffByUserIdAndType =
            gateDataApi.updateUserNoticeOnOffByUserIdAndType(requestBody)
        emit(updateUserNoticeOnOffByUserIdAndType)
    }

    /**
     * 查询用户通知开关
     */
    fun queryUserNoticeOnOffByUserId() {
        viewModelScope.launch {
            getPushSettingListRequest()
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _getPushSettingList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == BaseConstant.REQUEST_SUCCESS2 && it.data != null) {
                        _getPushSettingList.value = Result.Success(it.data)
                    } else {
                        _getPushSettingList.value = Result.Error(Exception("${it.code},${it.msg}"))
                    }
                }
        }
    }

    /**
     * 更新用户开关
     */
    fun updateUserNoticeOnOffByUserIdAndType(moduleId: String?, msgControl: String?) {
        val map = mutableMapOf<String, String>()
        map["moduleId"] = moduleId!!
        map["msgControl"] = "$msgControl"
        val toJSONString = JSON.toJSONString(map)
        loge("更新用户开关:$toJSONString")
        requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        viewModelScope.launch {
            updatePushSettingListRequest()
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _updatePushSettingList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == BaseConstant.REQUEST_SUCCESS2) {
                        _updatePushSettingList.value = Result.Success(it)
                    } else {
                        _updatePushSettingList.value =
                            Result.Error(Exception("${it.code},${it.message}"))
                    }
                }
        }
    }

}