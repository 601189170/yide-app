package com.yyide.chatim.viewmodel.gate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.NetworkUtils
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.gate.*
import com.yyide.chatim.net.GateDataService
import com.yyide.chatim.utils.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.lang.RuntimeException
import com.yyide.chatim.viewmodel.BaseViewModel
import java.lang.Exception

/**
 *
 * @author liu tao
 * @date 2022/1/5 13:51
 * @description 管理员相关接口
 */
class AdminViewModel : BaseViewModel() {
    private val _studentBaseInfo =
        MutableStateFlow<Result<GateBaseInfoBean>>(Result.Success(null))
    val studentBaseInfo: StateFlow<Result<GateBaseInfoBean>> = _studentBaseInfo

    private val _sectionList =
        MutableStateFlow<Result<List<BarrierSectionBean>>>(Result.Success(null))
    val sectionList: StateFlow<Result<List<BarrierSectionBean>>> = _sectionList

    private val _teacherThroughDeptList = MutableStateFlow<Result<StaffBean>>(Result.Success(null))
    val teacherThroughDeptList: StateFlow<Result<StaffBean>> = _teacherThroughDeptList

    lateinit var requestBody: RequestBody

    /**
     * 获取首页基础信息
     */
    fun queryBasicsInfoBySiteId(queryTime: String, siteId: String) {
        viewModelScope.launch {
            val body = mapOf("queryTime" to queryTime, "siteId" to siteId)
            val toJSONString = JSON.toJSONString(body)
            loge("获取首页基础信息：$toJSONString")
            requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
            baseInfoBySiteId
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _studentBaseInfo.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _studentBaseInfo.value = Result.Success(it.data)
                    } else {
                        _studentBaseInfo.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }

    /**
     * 获取首页学段统计信息
     */
    fun queryBarrierSectionStatistical(queryTime: String, siteId: String) {
        viewModelScope.launch {
            val body = mapOf("queryTime" to queryTime, "siteId" to siteId)
            val toJSONString = JSON.toJSONString(body)
            loge("获取首页学段统计信息：$toJSONString")
            requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
            barrierSectionList
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _sectionList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _sectionList.value = Result.Success(it.data)
                    } else {
                        _sectionList.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }

    /**
     * 查询闸机统计教职工出入数--部门
     */
    fun queryTeacherBarrierPassageDataByDeptId(deptId: String?, queryTime: String?, siteId: String?) {
        val body = mapOf("deptId" to deptId, "queryTime" to queryTime, "siteId" to siteId)
        val toJSONString = JSON.toJSONString(body)
        loge("查询闸机统计教职工出入数--部门：$toJSONString")
        requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        viewModelScope.launch {
            teacherBarrierPassageDataRequest
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _teacherThroughDeptList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _teacherThroughDeptList.value = Result.Success(it.data)
                    } else {
                        _teacherThroughDeptList.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }

    /**
     * 获取首页基础信息接口
     */
    private val baseInfoBySiteId: Flow<GateBaseRsp<GateBaseInfoBean>> = flow {
        networkExceptionHandler()
        val queryBasicsInfoBySiteId =
            GateDataService.gateDataApi.queryBasicsInfoBySiteId(requestBody)
        emit(queryBasicsInfoBySiteId)
    }

    private val barrierSectionList: Flow<GateBaseRsp<List<BarrierSectionBean>>> = flow {
        networkExceptionHandler()
        val queryBarrierSectionStatistical =
            GateDataService.gateDataApi.queryBarrierSectionStatistical(requestBody)
        emit(queryBarrierSectionStatistical)
    }

    //教职工列表数据--层级
    private val teacherBarrierPassageDataRequest: Flow<GateBaseRsp<StaffBean>> = flow {
        networkExceptionHandler()
        val queryTeacherBarrierPassageDataByDeptId =
            gateDataApi.queryTeacherBarrierPassageDataByDeptId(requestBody)
        emit(queryTeacherBarrierPassageDataByDeptId)
    }

}