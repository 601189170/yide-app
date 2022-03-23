package com.yyide.chatim.viewmodel.gate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.gate.ClassListOfTeacherBean
import com.yyide.chatim.model.gate.GateBaseRsp
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.SiteBean
import com.yyide.chatim.net.GateDataService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.RuntimeException

/**
 *
 * @author liu tao
 * @date 2021/12/31 10:48
 * @description 闸机场地数据
 */
class GateSiteViewModel : ViewModel() {
    private val _siteDataList = MutableStateFlow<Result<List<SiteBean>>>(Result.Success(null))
    val siteDataList: StateFlow<Result<List<SiteBean>>> = _siteDataList

    //当前选择的场地id
    val curSiteId: MutableLiveData<String> by lazy { MutableLiveData() }
    fun siteList() {
        viewModelScope.launch {
            siteList
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    _siteDataList.value = Result.Error(exception)
                }
                .collect {
                    if (it.code == BaseConstant.REQUEST_SUCCESS2 && it.data != null) {
                        _siteDataList.value = Result.Success(it.data)
                    } else {
                        _siteDataList.value = Result.Error(Exception("${it.msg}"))
                    }
                }
        }
    }

    private val siteList: Flow<GateBaseRsp<List<SiteBean>>> = flow {
        if (!NetworkUtils.isConnected()) throw RuntimeException("网络未连接")
        val siteList1 = GateDataService.gateDataApi.siteList()
        emit(siteList1)
    }
}