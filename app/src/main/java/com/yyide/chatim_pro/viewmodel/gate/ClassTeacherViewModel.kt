package com.yyide.chatim_pro.viewmodel.gate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.yyide.chatim_pro.model.gate.ClassListOfTeacherBean
import com.yyide.chatim_pro.model.gate.Result
import com.yyide.chatim_pro.model.gate.GateBaseRsp
import com.yyide.chatim_pro.net.GateDataService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.RuntimeException

/**
 *
 * @author liu tao
 * @date 2021/12/30 17:30
 * @description 闸机数据 -- 班主任
 */
class ClassTeacherViewModel : ViewModel() {
    private val _classDataList =
        MutableStateFlow<Result<List<ClassListOfTeacherBean>>>(Result.Success(null))
    val classDataList: StateFlow<Result<List<ClassListOfTeacherBean>>> = _classDataList
    /**
     * 查询班级列表
     */
    fun queryClassInfoByTeacherId() {
        viewModelScope.launch {
            classList
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _classDataList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _classDataList.value = Result.Success(it.data)
                    } else {
                        _classDataList.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }

    //班级列表
    private val classList: Flow<GateBaseRsp<List<ClassListOfTeacherBean>>> = flow {
        if (!NetworkUtils.isConnected()) throw RuntimeException("网络未连接")
        val classListRsp = GateDataService.gateDataApi.queryClassInfoByTeacherId()
        emit(classListRsp)
    }
}