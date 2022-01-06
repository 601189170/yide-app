package com.yyide.chatim.viewmodel.gate

import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.gate.GateBaseRsp
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.StudentGradeInOutInfoBean
import com.yyide.chatim.net.GateDataService
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody

/**
 *
 * @author liu tao
 * @date 2022/1/6 14:08
 * @description 闸机组织架构 中间层
 */
class GateSecondBranchViewModel : BaseViewModel() {
    lateinit var requestBody: RequestBody
    private val _gateSecondBranchData =
        MutableStateFlow<Result<StudentGradeInOutInfoBean>>(Result.Success(null))
    val gateSecondBranchData: StateFlow<Result<StudentGradeInOutInfoBean>> = _gateSecondBranchData

    private val allStudentInOutInfo: Flow<GateBaseRsp<StudentGradeInOutInfoBean>> = flow {
        networkExceptionHandler()
        val queryAllStudentInOutInfo =
            GateDataService.gateDataApi.queryAllStudentInOutInfo(requestBody)
        emit(queryAllStudentInOutInfo)
    }

    /**
     * 全校通行数据/学段/年级
     * @param peopleType
     * @param queryTime
     * @param queryType
     */
    fun queryAllStudentInOutInfo(peopleType: Int?, queryTime: String?, queryType: Int?, id: String?) {
        val mapOf = mapOf(
            "peopleType" to peopleType,
            "queryTime" to queryTime,
            "queryType" to queryType,
            "id" to id,
        )
        val toJSONString = JSON.toJSONString(mapOf)
        loge("获取闸机通行的部门层级列表：$toJSONString")
        requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        viewModelScope.launch {
            allStudentInOutInfo
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _gateSecondBranchData.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _gateSecondBranchData.value = Result.Success(it.data)
                    } else {
                        _gateSecondBranchData.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }
}