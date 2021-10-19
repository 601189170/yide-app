package com.yyide.chatim.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.database.*
import com.yyide.chatim.kotlin.network.base.BaseResponse
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.ScheduleEvent
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.loge
import org.greenrobot.eventbus.EventBus
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @author liu tao
 * @date 2021/10/14 15:38
 * @description 日程管理
 *      1，日程本地化
 *      2，日程查询
 */
class ScheduleMangeViewModel : ViewModel() {
    private var apiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)
    val requestAllScheduleResult:MutableLiveData<Boolean> = MutableLiveData()
    //显示日期
    val curDateTime:MutableLiveData<DateTime> = MutableLiveData()
    init {
        curDateTime.value = DateTime.now()
    }
    fun getAllScheduleList() {
        apiStores.selectAllScheduleList()
            .enqueue(object : Callback<BaseResponse<List<ScheduleData>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<ScheduleData>>>,
                    response: Response<BaseResponse<List<ScheduleData>>>
                ) {
                    val body = response.body()
                    loge("onResponse $body")
                    if (body != null && body.code == 200 && body.data != null) {
                        //查询日程成功 并保存
                        requestAllScheduleResult.postValue(true)
                        insertScheduleToDb(body.data)
                        EventBus.getDefault().post(ScheduleEvent(ScheduleEvent.NEW_TYPE,true))
                        return
                    }
                    requestAllScheduleResult.postValue(false)
                }

                override fun onFailure(call: Call<BaseResponse<List<ScheduleData>>>, t: Throwable) {
                    loge("onFailure ${t.localizedMessage}")
                    requestAllScheduleResult.postValue(false)
                }
            })
    }

    fun selectAllScheduleList(): List<ScheduleWithParticipantAndLabel> {
        val scheduleWithParticipantAndLabelLists =
            AppDatabase.getInstance(BaseApplication.getInstance()).scheduleDao()
                .getScheduleWithParticipantAndLabelLists()
        loge("scheduleWithParticipantAndLabelLists ${scheduleWithParticipantAndLabelLists.size}")
        return scheduleWithParticipantAndLabelLists
    }

    /**
     * 保存日程所有数据到本地数据库
     *
     */

    private fun insertScheduleToDb(scheduls: List<ScheduleData>) {
        loge("scheduls ${JSON.toJSONString(scheduls)}")
        ScheduleDaoUtil.clearAll()
        scheduls.forEach {
            val scheduleId = it.id
            val scheduleBean = ScheduleBean()
            scheduleBean.id = it.id
            scheduleBean.name = it.name
            scheduleBean.type = it.type
            scheduleBean.isTop = it.isTop
            scheduleBean.remark = it.remark
            scheduleBean.filePath = it.filePath
            scheduleBean.isRepeat = it.isRepeat
            scheduleBean.status = it.status
            scheduleBean.rrule = JSON.toJSONString(it.rrule)
            scheduleBean.remindType = it.remindType
            scheduleBean.remindTypeInfo = it.remindTypeInfo
            scheduleBean.startTime = it.startTime
            scheduleBean.endTime = it.endTime
            scheduleBean.iconImg = it.iconImg
            scheduleBean.isAllDay = it.isAllDay
            scheduleBean.siteId = it.siteId
            scheduleBean.siteName = it.siteName
            scheduleBean.updateType = it.updateType
            scheduleBean.updateDate = it.updateDate
            scheduleBean.dayOfMonth = it.dayOfMonth
            scheduleBean.promoterName = it.promoterName
            val participantList = mutableListOf<ParticipantList>()
            it.participant.forEach {
                val participant = ParticipantList()
                participant.id = it.id
                participant.userId = it.userId
                participant.scheduleId = scheduleId
                participant.type = it.type
                //学生和教职工取值不一
                participant.userName = it.userName
                participant.scheduleCreatorId = scheduleId
                participantList.add(participant)
            }
            val labelList = mutableListOf<LabelList>()
            it.label.forEach {
                val label = LabelList()
                label.id = it.id
                label.labelName = it.labelName
                label.colorValue = it.colorValue
                label.scheduleCreatorId = scheduleId
                labelList.add(label)
            }

            //插入数据到本地数据库
            AppDatabase.getInstance(BaseApplication.getInstance()).scheduleDao()
                .insert(scheduleBean, participantList, labelList)
        }
    }
}