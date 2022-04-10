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
    var moduleId: String? = null,
    var moduleName: String = "",
    var msgControl: String = ""//控制开关 0开1关
)