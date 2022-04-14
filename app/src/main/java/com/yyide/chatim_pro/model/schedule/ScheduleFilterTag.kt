package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/18 14:10
 * @description 描述
 */
data class ScheduleFilterTag(
    //日程搜索过滤条件
    //日程类型集合
    var types: List<Int>,
    //状态
    var status: List<Int>,
    //日期
    var startDate: String?,
    var endDate: String?,
    //标签集合
    var tags: List<LabelListRsp.DataBean>
)

data class TagType(
    //0类型1标签
    var type: Int,
    var label: LabelListRsp.DataBean?,
    var scheduleType: Int
)

// 查询接口需要参数
//{
//    "labelId":[1,2],
//    "type":[1,2],
//    "name": "日程",
//    "startTime":"",
//    "endTime":"",
//    "status":[1]
//}
data class FilterTagCollect(
    var name: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var type: List<Int>? = null,
    var labelId: List<String>? = null,
    var status: List<Int>? = null
)