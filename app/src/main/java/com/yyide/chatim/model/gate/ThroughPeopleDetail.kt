package com.yyide.chatim.model.gate

/**
 * @author liu tao
 * @date 2022/1/5 20:39
 * @description 闸机通行人员详情
 */
data class ThroughPeopleDetail(
    var userId: String? = null,
    var name: String? = null,
    var number: Int = 0,
    var earliestTime: String? = null,
    var earliestAddress: String? = null,
    var image: String? = null,
    var latestTime: String? = null,
    var latestAddress: String? = null,
    var trajectoryInfo: List<TrajectoryInfoBean>? = null
) {
    data class TrajectoryInfoBean (
        var siteName: String? = null,
        var machineName: String? = null,
        var inOutTime: String? = null,
        var inOut: String? = null
    )
}