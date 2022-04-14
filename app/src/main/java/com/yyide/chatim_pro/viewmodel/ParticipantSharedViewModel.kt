package com.yyide.chatim_pro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyide.chatim_pro.model.schedule.ParticipantRsp

/**
 *
 * @author liu tao
 * @date 2021/9/28 17:31
 * @description 描述
 */
class ParticipantSharedViewModel : ViewModel() {
    val curStaffParticipantList: MutableLiveData<MutableList<ParticipantRsp.DataBean.ParticipantListBean>> by lazy {
        MutableLiveData()
    }

    init {
        curStaffParticipantList.value = mutableListOf()
    }
}