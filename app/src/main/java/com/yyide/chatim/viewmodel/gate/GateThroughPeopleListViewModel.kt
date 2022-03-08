package com.yyide.chatim.viewmodel.gate

import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.gate.GateBaseRsp
import com.yyide.chatim.model.gate.GateThroughPeopleListBean
import com.yyide.chatim.model.gate.Result
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
 * @date 2022/1/5 18:34
 * @description 全部、学段、年级、班级详情
 */
class GateThroughPeopleListViewModel : BaseViewModel() {
    private val _gateThroughPeopleList =
        MutableStateFlow<Result<GateThroughPeopleListBean>>(Result.Success(null))
    val gateThroughPeopleList: StateFlow<Result<GateThroughPeopleListBean>> = _gateThroughPeopleList
    lateinit var requestBody: RequestBody

    /**
     * 全部、学段、年级、班级详情
     * @param peopleType 人员类型 1 学生 2教职工 3访客 访客暂时没有预留字段
     * @param queryType 1全部 2 学段 3 年级 4班级 5 个人 6 部门 7 学生全校 8 教职工全校
     * @param id 这个id和queryType结合起来使用 比如 查询是的学段 就是学段id 如果查询的是年级 就是年级id
     */
    fun queryAllStudentPassageInOutDetails(
        peopleType: Int?,
        queryTime: String?,
        queryType: Int?,
        id: String?,
        siteId: String?
    ) {
        val mapOf = mapOf(
            "peopleType" to peopleType,
            "queryTime" to queryTime,
            "queryType" to queryType,
            "id" to id,
            "buildingId" to siteId
        )
        val toJSONString = JSON.toJSONString(mapOf)
        loge("获取闸机通行的人员列表：$toJSONString")
        requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        viewModelScope.launch {
            throughPeopleList.flowOn(Dispatchers.Default)
                .catch { exception ->
                    _gateThroughPeopleList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _gateThroughPeopleList.value = Result.Success(it.data)
                    } else {
                        _gateThroughPeopleList.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }

    /**
     * 查询所有教职工通行数据
     */
    fun queryAllTeacherPassageInOutDetails(queryTime:String?,siteId:String?){
        val mapOf = mapOf(
            "queryTime" to queryTime,
            "buildingId" to siteId
        )
        val toJSONString = JSON.toJSONString(mapOf)
        loge("获取闸机通行的人员列表：$toJSONString")
        requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        viewModelScope.launch {
            throughStaffListRequest
                .flowOn(Dispatchers.Default)
                .catch { exception->
                    _gateThroughPeopleList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == 200 && it.data != null) {
                        _gateThroughPeopleList.value = Result.Success(it.data)
                    } else {
                        _gateThroughPeopleList.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }

    private val throughPeopleList: Flow<GateBaseRsp<GateThroughPeopleListBean>> = flow {
        networkExceptionHandler()
        val queryAllStudentPassageInOutDetails =
            GateDataService.gateDataApi.queryAllStudentPassageInOutDetails(requestBody)
        emit(queryAllStudentPassageInOutDetails)
    }
    //所有教职工
    private val throughStaffListRequest: Flow<GateBaseRsp<GateThroughPeopleListBean>> = flow {
        networkExceptionHandler()
        val queryAllTeacherPassageInOutDetails =
            gateDataApi.queryAllTeacherPassageInOutDetails(requestBody)
        emit(queryAllTeacherPassageInOutDetails)
    }
}