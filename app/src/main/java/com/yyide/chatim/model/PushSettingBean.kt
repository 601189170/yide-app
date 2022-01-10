package com.yyide.chatim.model

/**
 * @author liu tao
 * @date 2022/1/10 17:51
 * @description 描述
 */
data class PushSettingBean(
    var offFormList: List<OffFormListBean>? = null,
    var messageTypeFormList: List<MessageTypeFormListBean>? = null
) {
    data class OffFormListBean(
        var type: String? = null,
        var onOff: String? = null
    )

    data class MessageTypeFormListBean(
        var type: String? = null
    )
}