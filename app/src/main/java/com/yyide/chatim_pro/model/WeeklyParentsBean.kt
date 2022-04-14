package com.yyide.chatim_pro.model

import com.alibaba.fastjson.annotation.JSONField


data class WeeklyParentsBean(
    @JSONField(name = "attend")
    var attend: List<WeeklyParentsAttend>,
    @JSONField(name = "eval")
    var eval: Eval?,
    @JSONField(name = "expend")
    var expend: WeeklyParentsExpend?,
    @JSONField(name = "summary")
    var summary: WeeklyParentsSummary?,
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

data class Eval(
    @JSONField(name = "body")
    var body: String,
    @JSONField(name = "level")
    var level: Int//level：1:非常优秀、2:比较优秀、3：较好
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