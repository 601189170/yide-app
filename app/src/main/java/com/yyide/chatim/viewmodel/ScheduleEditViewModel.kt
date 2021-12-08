package com.yyide.chatim.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.model.BaseRsp
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil
import com.yyide.chatim.utils.loge
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 *
 * @author liu tao
 * @date 2021/9/23 16:27
 * @description 日程添加，修改，删除
 */
class ScheduleEditViewModel : ViewModel() {
    //日程标题
    val scheduleTitleLiveData = MutableLiveData<String>()
    //日程的状态
    val scheduleStatusLiveData = MutableLiveData<String>()
    //日程id
    val scheduleIdLiveData = MutableLiveData<String>()
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
    //场地
    val siteLiveData = MutableLiveData<SiteNameRsp.DataBean>()
    //参与人选择
    val participantList = MutableLiveData<List<ParticipantRsp.DataBean.ParticipantListBean>>(mutableListOf())
    //备注
    val remarkLiveData = MutableLiveData<String>()
    //更新类型
    val updateTypeLiveData = MutableLiveData<String>()
    val saveOrModifyResult = MutableLiveData<Boolean>()
    val deleteResult = MutableLiveData<Boolean>()
    val changeStatusResult = MutableLiveData<Boolean>()
    private var dingApiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)

    init {
        //初始化一个简单日程需要的数据
        startTimeLiveData.value = DateTime.now().toString("yyyy-MM-dd HH:mm:ss")
        endTimeLiveData.value = DateTime.now().toString("yyyy-MM-dd ") + "23:59:59"
        //默认不提醒
        remindLiveData.value = Remind.getNotRemind()
        repetitionLiveData.value = Repetition.getNotRepetition()
    }
    /**
     * 日程新增
     * @param type 日程类型【0：校历日程，1：课表日程，2：事务日程】
     * @param isRepetition 是否重复 0：否，1：是
     * @param remindType 日程提醒类型【0：非全天提醒，1：全天提醒】
     * @param remindTypeInfo 提醒周期【0：5分钟前、1：10分钟前、2：15分钟前、3：30分钟前、4：1小时前、
     * 5：2小时前、6：1天前、7：2天前、8：1周前、9：日程开始时】
     * @param isAllDay 是否是全天日程【0：不是，1：是】
     */
    fun saveOrModifySchedule(modify:Boolean = false){
        val scheduleTitle = scheduleTitleLiveData.value
        if (TextUtils.isEmpty(scheduleTitle)){
            ToastUtils.showShort("你还没告诉我您要准备做什么！")
            return
        }
        //val label:List<String?>? = labelListLiveData.value?.map { it.id }
        val allDay = allDayLiveData.value?:false
        var startTime = startTimeLiveData.value?:""
        var endTime = endTimeLiveData.value?:""
        val startTimeToDateTime = ScheduleDaoUtil.toDateTime(startTime)
        val endTimeToDateTime = ScheduleDaoUtil.toDateTime(endTime)
        if (startTimeToDateTime == endTimeToDateTime){
            endTime = startTimeToDateTime.toString("yyyy-MM-dd ") + "23:59:59"
        }

        if (allDay){
            startTime = startTimeToDateTime.toString("yyyy-MM-dd ") + "00:00:00"
            endTime = endTimeToDateTime.toString("yyyy-MM-dd ") + "23:59:59"
        }

        val remind = remindLiveData.value?.id
        val repetition = repetitionLiveData.value
        if (repetition?.rule != null){
            repetition.rule?.set("dtstart",startTime)
        }
        val scheduleData = ScheduleData()
        if (modify){
            scheduleData.id = scheduleIdLiveData.value
            if (updateTypeLiveData.value != null){
                scheduleData.updateType = updateTypeLiveData.value
                scheduleData.updateDate = startTime//DateUtils.switchTime(Date(),"yyyy-MM-dd")
            }
        }
        scheduleData.name = scheduleTitle?:""
        scheduleData.status = scheduleStatusLiveData.value
        scheduleData.type = "2"
        scheduleData.isRepeat = repetition?.code?.toString()?:"0"
        scheduleData.rrule = if (repetition?.rule?.isEmpty() == true) null else repetition?.rule
        scheduleData.remindType = if (allDay) "1" else "0"
        scheduleData.remindTypeInfo = remind?:"9"
        scheduleData.startTime = startTime
        scheduleData.endTime = endTime
        scheduleData.isAllDay = if (allDay) "1" else "0"
        scheduleData.label = labelListLiveData.value
        scheduleData.participant = participantList.value
        scheduleData.siteId = siteLiveData.value?.id
        scheduleData.remark = remarkLiveData.value
        //截止日期不能小于开始日期
        if (scheduleData.rrule != null){
            val until = scheduleData.rrule["until"]
            until?.let {
                if (!TextUtils.isEmpty(it.toString())){
                    val startTimeDateTime = ScheduleDaoUtil.toDateTime(scheduleData.startTime)
                    val untilDateTime = ScheduleDaoUtil.toDateTime(until.toString())
                    if (startTimeDateTime>=untilDateTime){
                        ToastUtils.showShort("提交失败，截止日期不能小于日程开始日期！")
                        return
                    }
                }
            }
        }
        //不支持跨天且重复日程
        if (ScheduleRepetitionRuleUtil.moreDayAndRepetitionData(scheduleData.isRepeat,scheduleData.startTime,scheduleData.endTime)){
            ToastUtils.showShort("暂时不支持跨天重复日程！")
            return
        }

        val toJSONString = JSON.toJSONString(scheduleData)
        loge("toJSONString=$toJSONString")
        val body = RequestBody.create(BaseConstant.JSON, toJSONString)
        dingApiStores.saveSchedule(body).enqueue(object : Callback<BaseRsp> {
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                val body1 = response.body()
                loge("${body1}")
                if (body1 != null && body1.code == 200){
                    if (!modify){
                        EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA,""))
                        ToastUtils.showShort("日程添加成功")
                    }
                    saveOrModifyResult.postValue(true)
                    return
                }
                if (!modify){
                    ToastUtils.showShort("日程添加失败")
                }

                saveOrModifyResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                if (!modify){
                    ToastUtils.showShort("日程添加失败")
                }
                saveOrModifyResult.postValue(false)
            }
        })
    }

    fun curScheduleData():ScheduleData{
        val allDay = allDayLiveData.value?:false
        val startTime = startTimeLiveData.value
        val endTime = endTimeLiveData.value
        val remind = remindLiveData.value?.id
        val repetition = repetitionLiveData.value?.rule
        val scheduleData = ScheduleData()
        scheduleData.id = scheduleIdLiveData.value
        if (updateTypeLiveData.value != null){
            scheduleData.updateType = updateTypeLiveData.value
            scheduleData.updateDate = DateUtils.switchTime(Date(),"yyyy-MM-dd")
        }
        val scheduleTitle = scheduleTitleLiveData.value
        scheduleData.name = scheduleTitle?:""
        scheduleData.status = scheduleStatusLiveData.value
        scheduleData.type = "2"
        scheduleData.isRepeat = if (repetition == null) "0" else "1"
        scheduleData.rrule = repetition
        scheduleData.remindType = if (allDay) "1" else "0"
        scheduleData.remindTypeInfo = remind?:""
        scheduleData.startTime = startTime?:""
        scheduleData.endTime = endTime?:""
        scheduleData.isAllDay = if (allDay) "1" else "0"
        scheduleData.label = labelListLiveData.value
        scheduleData.participant = participantList.value
        scheduleData.siteId = siteLiveData.value?.id
        scheduleData.remark = remarkLiveData.value
        return scheduleData
    }

    /**
     * 删除日程
     */
    fun deleteScheduleById(id:String){
        dingApiStores.deleteScheduleById(id).enqueue(object :Callback<BaseRsp>{
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                val body = response.body()
                if (body != null && body.code == 200){
                    ToastUtils.showShort("删除日程成功")
                    deleteResult.postValue(true)
                    return
                }
                ToastUtils.showShort("删除日程失败")
                deleteResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                ToastUtils.showShort("删除日程失败")
                deleteResult.postValue(false)
            }
        })
    }

    /**
     * 修改日程状态
     */
    fun changeScheduleState(scheduleData: ScheduleData){
        if (scheduleData.status == "1") {
            scheduleData.status = "0"
        } else {
            scheduleData.status = "1"
        }
        val toJSONString = JSON.toJSONString(scheduleData.simpleScheduleData())
        val requestBody = RequestBody.create(BaseConstant.JSON, toJSONString)
        dingApiStores.changeScheduleState(requestBody).enqueue(object :Callback<BaseRsp>{
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                val body = response.body()
                if (body != null && body.code == 200){
                    changeStatusResult.postValue(true)
                    return
                }
                changeStatusResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                changeStatusResult.postValue(false)
            }
        })
    }
}

fun ScheduleEditViewModel.toScheduleDataBean(): ScheduleDataBean {
    val scheduleDataBean = ScheduleDataBean()
    scheduleDataBean.name = this.scheduleTitleLiveData.value
    scheduleDataBean.allDay = this.allDayLiveData.value ?: false
    scheduleDataBean.startTime = this.startTimeLiveData.value
    scheduleDataBean.endTime = this.endTimeLiveData.value
    scheduleDataBean.remind = this.remindLiveData.value
    scheduleDataBean.repetition = this.repetitionLiveData.value
    scheduleDataBean.labelList = this.labelListLiveData.value
    scheduleDataBean.participantList = this.participantList.value
    scheduleDataBean.siteName = this.siteLiveData.value
    scheduleDataBean.remark = this.remarkLiveData.value
    return scheduleDataBean
}

fun ScheduleDataBean.toScheduleEditViewModel(scheduleEditViewModel: ScheduleEditViewModel):ScheduleEditViewModel{
    scheduleEditViewModel.scheduleTitleLiveData.value = name
    scheduleEditViewModel.allDayLiveData.value = allDay
    scheduleEditViewModel.startTimeLiveData.value = startTime
    scheduleEditViewModel.endTimeLiveData.value = endTime
    scheduleEditViewModel.remindLiveData.value = remind
    scheduleEditViewModel.repetitionLiveData.value = repetition
    scheduleEditViewModel.labelListLiveData.value = labelList
    scheduleEditViewModel.participantList.value = participantList
    scheduleEditViewModel.siteLiveData.value = siteName
    scheduleEditViewModel.remarkLiveData.value = remark
    return scheduleEditViewModel
}

fun ScheduleEditViewModel.clearData(){
    scheduleTitleLiveData.value = null
    allDayLiveData.value = null
    startTimeLiveData.value = null
    endTimeLiveData.value = null
    remindLiveData.value = null
    repetitionLiveData.value = null
    labelListLiveData.value = null
    participantList.value = null
    siteLiveData.value = null
    remarkLiveData.value = null
}