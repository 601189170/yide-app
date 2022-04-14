package com.yyide.chatim_pro.model.message

/**
 *
 * @author shenzhibin
 * @date 2022/4/8 16:44
 * @description 信息发布的event message
 * type 0是收到消息 1是分发消息
 */
data class EventMessageBean(var type: Int = 0, var item: AcceptMessageItem = AcceptMessageItem())
