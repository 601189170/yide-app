package com.yyide.chatim.model

/**
 * @author liu tao
 * @date 2022/1/10 17:51
 * @description
 */
data class PushSettingBean(
    //"id": "1",
    //"onOff": true,
    //"sort": 1,
    //"name": "闸机推送",
    //"switchKey": "barrier",
    //"delInd": false
    //}
    var id: String? = null,
    var onOff: Boolean = true,
    var sort: Int = 1,
    var name: String? = null,
    var switchKey: String? = null,
    var delInd: Boolean = false
)