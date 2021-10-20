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
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.net.DingApiStores
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 *
 * @author liu tao
 * @date 2021/10/20 13:38
 * @description 描述
 */
class TimetableViewModel : ViewModel() {
    //课表日程数据
    val scheduleData: MutableLiveData<ScheduleData> = MutableLiveData()
    private var dingApiStores: DingApiStores =
        AppClient.getDingRetrofit().create(DingApiStores::class.java)
    val saveOrModifyResult = MutableLiveData<Boolean>()

    /**
     * 修改课表日程
     */
    fun modifySchedule() {
        val toJSONString = JSON.toJSONString(scheduleData.value)
        loge("toJSONString=$toJSONString")
        val body = RequestBody.create(BaseConstant.JSON, toJSONString)
        dingApiStores.saveSchedule(body).enqueue(object : Callback<BaseRsp> {
            override fun onResponse(call: Call<BaseRsp>, response: Response<BaseRsp>) {
                val body1 = response.body()
                loge("${body1}")
                if (body1 != null && body1.code == 200) {
                    saveOrModifyResult.postValue(true)
                    return
                }
                saveOrModifyResult.postValue(false)
            }

            override fun onFailure(call: Call<BaseRsp>, t: Throwable) {
                saveOrModifyResult.postValue(false)
            }
        })
    }
}