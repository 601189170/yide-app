package com.yyide.chatim.activity.operation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.TeacherWorkListRsp
import com.yyide.chatim.model.schedule.ScheduleData
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class OperationTearcherViewModel : ViewModel() {

    val TeacherWorkListLiveData = MutableLiveData<Result<TeacherWorkListRsp>>()

    /**
     * 教职工作业列表
     * isWhole	是	string	0全部作业 1:我发布的
        subjectId	是	string	课程id
        classesId	是	string	班级id
        startTime	是	string	开始时间
        endTime	是	string	结束时间
        pageNo	是	string	第几页
        pageSize	是	string	每页显示数量
     */
    fun getTecherWorkList(isWhole: String, subjectId: String, classesId: String, startTime: String,endTime :String,pageNo :String,pageSize:String){
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            map["isWhole"] = isWhole
            map["subjectId"] = subjectId
            map["classesId"] = classesId
            map["startTime"] = startTime
            map["endTime"] = endTime
            map["pageNo"] = pageNo
            map["pageSize"] = pageSize
            val body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map))
            val result = NetworkApi.requestTeacherWorkList(body)
            TeacherWorkListLiveData.value = result
        }
    }
}