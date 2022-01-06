package com.yyide.chatim.model.gate

/**
 * @author liu tao
 * @date 2022/1/5 18:10
 * @description 全部、学段、年级、班级详情
 */
data class GateThroughPeopleListBean(
    var totalNumber: Int = 0,
    var outNumber: Int = 0,
    var intoNumber: Int = 0,
    var title: String? = null,
    var peopleList: List<PeopleListBean>? = null
) {
    data class PeopleListBean(
        var type: Int,//类型 3列 4列 2列
        val deptName: String? = null,
        var studentName: String? = null,
        var className: String? = null,
        var siteName: String? = null,
        var userId: String? = null,
        var inOut: String? = null,//1出2入
        var addTime: String? = null,
        var sectionId: String? = null,
        var gradeId: String? = null,
        var classId: String? = null,
    )
}