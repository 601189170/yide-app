package com.yyide.chatim.model.gate

/**
 * @author liu tao
 * @date 2022/1/6 14:02
 * @description 描述
 */
data class StudentGradeInOutInfoBean(
    var basicsForm: BasicsFormBean? = null,
    var studentDataList: List<GateBaseInfoBean>? = null,
    var navigation: NavigationBean? = null
) {
    data class BasicsFormBean(
        var totalNumber: Int = 0,
        var outNumber: Int = 0,
        var intoNumber: Int = 0,
        var name: String? = null,
        var id: String? = null,
    )

    data class NavigationBean(
        var sectionId: String? = null,
        var sectionName: String? = null,
        var gradeId: String? = null,
        var gradeName: String? = null,
        var classId: String? = null,
        var className: String? = null,
        var type: Int = 0
    )

//    data class StudentDataListBean(
//        var totalNumber: Int = 0,
//        var outNumber: Int = 0,
//        var intoNumber: Int = 0,
//        var id: String? = null,
//        var name: String? = null,
//        var type: String? = null
//    )
}