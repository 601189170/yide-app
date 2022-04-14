package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/11/30 14:23
 * @description 描述
 */
data class ScheduleDataBean(
    var name: String? = null,
    //是否是全天日程
    var allDay: Boolean = false,
    //日程开始时间
    var startTime: String? = null,
    //日程结束时间
    var endTime: String? = null,
    //日程提醒
    var remind: Remind? = null,
    //日程重复(为空时，说明不重复，不为空说明重复，并且值是重复规则)
    var repetition: Repetition? = null,
    //标签
    var labelList: List<LabelListRsp.DataBean>? = null,
    //场地
    var siteName: SiteNameRsp.DataBean? = null,
    //参与人选择
    var participantList: List<ParticipantRsp.DataBean.ParticipantListBean>? = null,
    //备注
    var remark: String? = null

)