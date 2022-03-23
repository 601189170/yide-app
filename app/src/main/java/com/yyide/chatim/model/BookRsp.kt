package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

data class BookRsp(
    @JSONField(name = "code")
    var code: Int,
    @JSONField(name = "data")
    var `data`: Data,
    @JSONField(name = "msg")
    var msg: String,
    @JSONField(name = "success")
    var success: Boolean
)

data class Data(
    @JSONField(name = "schoolName")
    var schoolName_: String,
    @JSONField(name = "appSchoolName")
    var appSchoolName: String,
    @JSONField(name = "schoolBadgeImg")
    var schoolBadgeImg: String,
    @JSONField(name = "classesList")
    var classesList: MutableList<BookClassesItem>,//班级列表
    @JSONField(name = "teacherList")
    var teacherList: MutableList<BookTeacherItem>,//老师列表
    @JSONField(name = "departmentList")
    var departmentList: MutableList<BookDepartmentItem>,//部门
)

/**
 * 班级实体
 */
data class BookClassesItem(
    @JSONField(name = "id")
    var id: Long = -1,
    @JSONField(name = "name")
    var name: String = "",
    @JSONField(name = "studentList")
    var studentList: List<BookStudentItem> = emptyList(),
    @JSONField(name = "teacherList")
    var teacherList: List<BookTeacherItem> = emptyList(),
    var guardianList: List<BookGuardianItem> = emptyList(),
    var departmentList: List<BookDepartmentItem> = emptyList(),//部门
    var elternAddBookDTOList: List<Parent> = emptyList(),//部门
    var unfold: Boolean = false,//是否展开
    var student: BookStudentItem? = null,
    var guardian: BookGuardianItem? = null,
    var guardian2: Parent? = null,
    override var itemType: Int = 0
) : MultiItemEntity {
}

/**
 * 学生实体
 */
data class BookStudentItem(
    @JSONField(name = "id")
    var id: Long?,
    @JSONField(name = "name")
    var name: String?,
    @JSONField(name = "phone")
    var phone: String?,//监护人ID
    @JSONField(name = "className")
    var className: String?,
    @JSONField(name = "userId")
    var userId: String?,
    @JSONField(name = "primaryGuardianPhone")
    var primaryGuardianPhone: String?,//	性别（1:男、0:女）
    @JSONField(name = "deputyGuardianPhone")
    var deputyGuardianPhone: String?,
    @JSONField(name = "sex")
    var sex: String?,
    @JSONField(name = "address")
    var address: String?,//家长：是否自己的孩子 0不是，1是
    @JSONField(name = "faceInformation")
    var faceInformation: String?,
    @JSONField(name = "isOwnChild")
    var isOwnChild: String?,
//    @JSONField(name = "guardianList")
//    var guardianList: MutableList<BookGuardianItem>,
        @JSONField(name = "guardianList")
var elternAddBookDTOList: MutableList<Parent>,
        @JSONField(name = "avatar")
var avatar: String?,
    //学生监护人信息){}){}){}
) : Serializable



/**
 * 监护人实体
 */
data class BookGuardianItem(
    @JSONField(name = "name")
    var name: String?,
    @JSONField(name = "id")
    var id: Long?,
    @JSONField(name = "phone")
    var phone: String?,
    @JSONField(name = "userId")
    var userId: String?,//监护人ID
    @JSONField(name = "relation")
    var relation: String?,//	与监护人关系（0:父亲，1:母亲，2:爷爷，3:奶奶，4:外公，5:外婆，6:其它）
    @JSONField(name = "workUnit")
    var workUnit: String?,//工作单位
    @JSONField(name = "faceInformation")
    var faceInformation: String?,//头像
    @JSONField(name = "singleParent")
    var singleParent: String?//是否单亲监护（0:否，1:是）
) : Serializable {
    @JvmName("getRelation1")
    fun getRelation(): String {
        return when (relation) {//	与监护人关系（0:父亲，1:母亲，2:爷爷，3:奶奶，4:外公，5:外婆，6:其它）
            "0" ->
                "父亲"
            "1" ->
                "母亲"
            "2" ->
                "爷爷"
            "3" ->
                "奶奶"
            "4" ->
                "外公"
            "5" ->
                "外婆"
            "6" ->
                "其他监护人"
            else ->
                "其他监护人"
        }
    }
}

/**
 * 教师
 */
data class BookTeacherItem(
    @JSONField(name = "name")
    var name: String?,
    @JSONField(name = "sex")
    var gender: String?,
    @JSONField(name = "phone")
    var phone: String?,
    @JSONField(name = "userId")
    var userId: String?,
    @JSONField(name = "email")
    var email: String?,
    @JSONField(name = "subjectName")
    var subjectName: String?,
    @JSONField(name = "teachingSubjects")
    var teachingSubjects: String?,
    @JSONField(name = "faceInformation")
    var faceInformation: String?,
    @JSONField(name = "whitelist")
    var whitelist: String?,
    @JSONField(name = "avatar")
    var avatar: String?

) : Serializable

/**
 * 组织架构部门
 */
data class BookDepartmentItem(
    @JSONField(name = "id")
    var id: Long,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "list")
    var list: MutableList<BookDepartmentItem>,
    @JSONField(name = "teacheList")
    var teacheList: MutableList<BookTeacherItem>,
    var level: Int = 0,
    var unfold: Boolean = false,//是否展开
    var parentId: Long = 0
)