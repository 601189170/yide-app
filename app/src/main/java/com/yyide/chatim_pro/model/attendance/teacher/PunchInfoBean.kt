package com.yyide.chatim_pro.model.attendance.teacher

/**
 *
 * @author shenzhibin
 * @date 2022/3/31 17:41
 * @description 描述
 */
data class PunchInfoBean(
    //打卡类别: 0不可打卡 1 地址 2 wifi 3外勤
    var type: Int = 0,
    var showContent: String ?= "超出管理员指定打卡范围"
)
