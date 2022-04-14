package com.yyide.chatim_pro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tencent.mmkv.MMKV
import com.yyide.chatim_pro.BaseApplication
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.base.MMKVConstant
import com.yyide.chatim_pro.database.AppDatabase
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.database.ScheduleWithParticipantAndLabel
import com.yyide.chatim_pro.database.scheduleDataToScheduleWithParticipantAndLabel
import com.yyide.chatim_pro.model.schedule.ScheduleData
import com.yyide.chatim_pro.model.schedule.ScheduleDataRsp
import com.yyide.chatim_pro.model.schedule.ScheduleEvent
import com.yyide.chatim_pro.net.AppClient
import com.yyide.chatim_pro.net.DingApiStores
import com.yyide.chatim_pro.utils.loge
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

    //月列表添加日程
    val monthAddSchedule:MutableLiveData<DateTime> = MutableLiveData()
    init {
        curDateTime.value = DateTime.now()
    }
    fun getAllScheduleList() {
        apiStores.selectAllScheduleList()
            .enqueue(object : Callback<ScheduleDataRsp> {
                override fun onResponse(
                    call: Call<ScheduleDataRsp>,
                    response: Response<ScheduleDataRsp>
                ) {
                    val body = response.body()
                    //loge("onResponse $body")
                    if (body != null && body.code == BaseConstant.REQUEST_SUCCES_0 && body.data != null) {
                        //查询日程成功 并保存
                        requestAllScheduleResult.postValue(true)
                        insertScheduleToDb(body.data.scheduleList)
                        val historyShow = body.data.historyShow
                        loge("是否显示历史日程 $historyShow")
                        MMKV.defaultMMKV().encode(MMKVConstant.YD_SHOW_HISTORY_SCHEDULE,historyShow)
                        EventBus.getDefault().post(ScheduleEvent(ScheduleEvent.NEW_TYPE,true))
                        return
                    }
                    requestAllScheduleResult.postValue(false)
                }

                override fun onFailure(call: Call<ScheduleDataRsp>, t: Throwable) {
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
    private fun insertScheduleToDb(scheduls: List<ScheduleData>?) {
        //loge("scheduls ${JSON.toJSONString(scheduls)}")
        ScheduleDaoUtil.clearAll()
        scheduls?.forEach {
            //插入数据到本地数据库
            ScheduleDaoUtil.insert(it.scheduleDataToScheduleWithParticipantAndLabel())
        }
    }
}