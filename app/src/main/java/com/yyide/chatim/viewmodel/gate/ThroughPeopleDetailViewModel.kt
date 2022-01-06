package com.yyide.chatim.viewmodel.gate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.NetworkUtils
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.gate.GateBaseRsp
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.StudentInfoBean
import com.yyide.chatim.model.gate.ThroughPeopleDetail
import com.yyide.chatim.net.GateDataService
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.lang.RuntimeException

/**
 *
 * @author liu tao
 * @date 2022/1/5 20:43
 * @description 闸机通行人员详情
 */
class ThroughPeopleDetailViewModel : BaseViewModel() {
    private val _throughPeopleDetail =
        MutableStateFlow<Result<ThroughPeopleDetail>>(Result.Success(null))
    val throughPeopleDetail: StateFlow<Result<ThroughPeopleDetail>> = _throughPeopleDetail

    private val _studentListInfo =
        MutableStateFlow<Result<List<StudentInfoBean>>>(Result.Success(null))
    val studentListInfo: StateFlow<Result<List<StudentInfoBean>>> = _studentListInfo

    lateinit var requestBody: RequestBody
    private val throughPeopleDetailRequest: Flow<GateBaseRsp<ThroughPeopleDetail>> = flow {
        networkExceptionHandler()
        val queryAllStudentPassageInOutDetails =
            GateDataService.gateDataApi.queryPersonalPassageDetails(requestBody)
        emit(queryAllStudentPassageInOutDetails)
    }

    private val studentInfoRequest: Flow<GateBaseRsp<List<StudentInfoBean>>> = flow {
        networkExceptionHandler()
        val queryStudentInfoByUserId = gateDataApi.queryStudentInfoByUserId()
        emit(queryStudentInfoByUserId)
    }

    /**
     * 家长视角查询学生列表
     */
    fun queryStudentInfoByUserId(){
        viewModelScope.launch {
            studentInfoRequest
                .flowOn(Dispatchers.Default)
                .catch { exception->
                    _studentListInfo.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null){
                        _studentListInfo.value = Result.Success(it.data)
                    }else{
                        _studentListInfo.value = Result.Error(Exception("${it.code} ${it.msg}"))
                    }
                }
        }
    }
    /**
     * 查询闸机通行人的详情
     * @param peopleType 人员类型 1 学生 2教职工 3访客
     */
    fun queryPersonalPassageDetails(
        peopleType: String?,
        queryTime: String?,
        userId: String?,
        siteId: String?
    ) {
        val mapOf = mapOf(
            "peopleType" to peopleType,
            "queryTime" to queryTime,
            "userId" to userId,
            "siteId" to siteId
        )
        val toJSONString = JSON.toJSONString(mapOf)
        loge("获取闸机通行的人员列表：$toJSONString")
        requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        viewModelScope.launch {
            throughPeopleDetailRequest
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _throughPeopleDetail.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _throughPeopleDetail.value = Result.Success(it.data)
                    } else {
                        _throughPeopleDetail.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }
}