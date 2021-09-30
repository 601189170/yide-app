package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField


data class WeeklyParentsBean(
    @JSONField(name = "attend")
    var attend: List<WeeklyParentsAttend>,
    @JSONField(name = "eval")
    var eval: String,
    @JSONField(name = "expend")
    var expend: WeeklyParentsExpend,
    @JSONField(name = "summary")
    var summary: WeeklyParentsSummary,
    @JSONField(name = "work")
    var work: Work
)

data class WeeklyParentsAttend(
    @JSONField(name = "abNumber")
    var abNumber: Int,
    @JSONField(name = "earlyNumber")
    var earlyNumber: Int,
    @JSONField(name = "lateNumber")
    var lateNumber: Int,
    @JSONField(name = "leaveNumber")
    var leaveNumber: Int,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "rate")
    var rate: Double
)

class WeeklyParentsExpend

data class WeeklyParentsSummary(
    @JSONField(name = "attend")
    var attend: String,
    @JSONField(name = "expend")
    var expend: String,
    @JSONField(name = "work")
    var work: String
)

class Work