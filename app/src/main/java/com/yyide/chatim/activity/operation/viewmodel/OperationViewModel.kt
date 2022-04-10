package com.yyide.chatim.activity.operation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class OperationViewModel : ViewModel() {
    //教职工列表数据
    val TeacherWorkListLiveData = MutableLiveData<Result<TeacherWorkListRsp>>()
    //获取科目
    val SubjectBeanData = MutableLiveData<Result<List<SubjectBean>>>()
    //获取年纪关联数据
    val getClassSubjectListRsp = MutableLiveData<Result<List<getClassSubjectListRsp>>>()
    //选择类型
//    val tv1data = MutableLiveData<selectBean>()
    //选择科目
    val tv2data = MutableLiveData<SubjectBean>()

    val subjectId = MutableLiveData<String>()

    val subjectName = MutableLiveData<String>()

    val classesId = MutableLiveData<String>()

    val classesName = MutableLiveData<String>()

    val startTime = MutableLiveData<String>()

    val endTime = MutableLiveData<String>()

    val ljName = MutableLiveData<String>()

    val rightData = MutableLiveData<rightData>()

    val leftData = MutableLiveData<selectBean>()

    val allDayLiveData = MutableLiveData<Boolean>(false)


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
    fun getTecherWorkList(isWhole: String, subjectId: String, classesId: String, startTime: String,endTime :String,pageNo:Int,pageSize:Int){
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

    /***
     * 获取科目
     */

    fun selectSubjectByUserId(){
        viewModelScope.launch {
            val result = NetworkApi.selectSubjectByUserId()
            Log.e("TAG", "selectSubjectByUserId: "+JSON.toJSONString(result) )
//            result.getOrNull()

            SubjectBeanData.value = result

        }
    }

    fun getClassSubjectList(isWhole:String){
        viewModelScope.launch {
            val result = NetworkApi.getClassSubjectList(isWhole)
            Log.e("TAG", "selectSubjectByUserId: "+JSON.toJSONString(result) )

            getClassSubjectListRsp.value = result
        }
    }
}