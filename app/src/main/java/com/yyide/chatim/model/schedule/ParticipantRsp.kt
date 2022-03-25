package com.yyide.chatim.model.schedule

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

/**
 * @author liu tao
 * @date 2021/9/28 13:35
 * @description 描述
 */
data class ParticipantRsp(
        val code: Int = 0,
        val success: Boolean = false,
        val msg: String? = null,
//    val data: DataBean? = null,
        val `data`: List<DataBean> ?= null,
) {
    data class DataBean(
        var name: String? = null,
        var id: String? = null,
        var type: String? = null,
        var list: List<ParticipantListBean>? = null,
        var personList: List<ParticipantListBean>? = null
    ) {
        data class ParticipantListBean(
            var id: String? = null,
            var userId:String?=null,
            var name: String? = "",
            var realname: String? = "",
            //var status:String? = null,
            @SerializedName("status")
            var myType:String? = null,
            var type: String? = null,
            var department:Boolean = false,
            var checked:Boolean = false,
            var guardians: List<GuardiansBean> = mutableListOf()
        ){
            data class GuardiansBean(
                val id:String? = null,
                var userId: String? = null,
                var guardianName: String? = null,
                var checked: Boolean = false,
                //0.父亲1.母亲2.爷爷3.奶奶4.外公5.外婆，6其他
                var relation:Int = 0
            )
        }
    }
}

fun ParticipantRsp.DataBean.ParticipantListBean.GuardiansBean.toParticipantListBean(): ParticipantRsp.DataBean.ParticipantListBean {
    val participantListBean = ParticipantRsp.DataBean.ParticipantListBean()
    participantListBean.id = this.id
    participantListBean.userId = this.userId
    participantListBean.realname = this.guardianName
    participantListBean.department = false
    participantListBean.name = this.guardianName
    return participantListBean
}