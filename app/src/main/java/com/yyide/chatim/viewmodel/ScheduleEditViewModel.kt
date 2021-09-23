package com.yyide.chatim.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.Remind
import com.yyide.chatim.model.schedule.Repetition
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.loge
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/9/23 16:27
 * @description 描述
 */
class ScheduleEditViewModel : ViewModel() {
    //日程标题
    val scheduleTitleLiveData = MutableLiveData<String>()
    //当前选择的标签
    val labelListLiveData = MutableLiveData<List<LabelListRsp.DataBean>>()
    //是否是全天日程
    val allDayLiveData = MutableLiveData<Boolean>()
    //日程开始时间
    val startTimeLiveData = MutableLiveData<String>()
    //日程结束时间
    val endTimeLiveData = MutableLiveData<String>()
    //日程提醒
    val remindLiveData = MutableLiveData<Remind>()
    //日程重复(为空时，说明不重复，不为空说明重复，并且值是重复规则)
    val repetitionLiveData = MutableLiveData<Repetition>()
    private var dingApiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)
    /**
     * 日程新增
     * @param type 日程类型【0：校历日程，1：课表日程，2：事务日程】
     * @param isRepetition 是否重复 0：否，1：是
     * @param remindType 日程提醒类型【0：非全天提醒，1：全天提醒】
     * @param remindTypeInfo 提醒周期【0：5分钟前、1：10分钟前、2：15分钟前、3：30分钟前、4：1小时前、
     * 5：2小时前、6：1天前、7：2天前、8：1周前、9：日程开始时】
     * @param isAllDay 是否是全天日程【0：不是，1：是】
     */
    fun saveSchedule(){
        val scheduleTitle = scheduleTitleLiveData.value
        if (TextUtils.isEmpty(scheduleTitle)){
            ToastUtils.showShort("你还没告诉我您要准备做什么！")
            return
        }
        //val label:List<String?>? = labelListLiveData.value?.map { it.id }
        val allDay = allDayLiveData.value?:false
        val startTime = startTimeLiveData.value
        val endTime = endTimeLiveData.value
        val remind = remindLiveData.value?.id
        val repetition = repetitionLiveData.value?.rule
        val scheduleData = ScheduleData()
        scheduleData.name = scheduleTitle?:""
        scheduleData.type = "2"
        scheduleData.isRepeat = if (TextUtils.isEmpty(repetition)) "0" else "1"
        scheduleData.rrule = repetition?:""
        scheduleData.remindType = if (allDay) "1" else "0"
        scheduleData.remindTypeInfo = remind?:""
        scheduleData.startTime = startTime?:""
        scheduleData.endTime = endTime?:""
        scheduleData.isAllDay = if (allDay) "1" else "0"
        scheduleData.label = labelListLiveData.value
        val toJSONString = JSON.toJSONString(scheduleData)
        loge("toJSONString=$toJSONString")
        val body = RequestBody.create(BaseConstant.JSON, toJSONString)
        dingApiStores.saveSchedule(body).enqueue(object : Callback<BaseRsp> {
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                val body1 = response.body()
                loge("${body1}")
                if (body1 != null && body1.code == 200){
                    ToastUtils.showShort("日程添加成功")
                    return
                }
                ToastUtils.showShort("日程添加失败")
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                ToastUtils.showShort("日程添加失败")
            }
        })
    }

    fun editSchedule(){

    }
}