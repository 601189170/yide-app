package com.yyide.chatim.model

import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable


data class BookSearchRsp(
    @JSONField(name = "code")
    var code: Int,
    @JSONField(name = "data")
    var `data`: DataBean,
    @JSONField(name = "msg")
    var msg: String,
    @JSONField(name = "success")
    var success: Boolean
)

data class DataBean(
    @JSONField(name = "studentList")
    var studentList: MutableList<Student>,
    @JSONField(name = "teacherList")
    var teacherList: MutableList<Teacher>
)

data class Student(
    @JSONField(name = "faceInformation")
    var faceInformation: String,
    @JSONField(name = "list")
    var list: BookSearchStudent,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "parentList")
    var parentList: MutableList<Parent>,
    @JSONField(name = "type")
    var type: Int,
    @JSONField(name = "typeName")
    var typeName: String
)

data class Teacher(
    @JSONField(name = "faceInformation")
    var faceInformation: String,
    @JSONField(name = "list")
    var list: BookTeacherItem,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "type")
    var type: Int,
    @JSONField(name = "typeName")
    var typeName: String
)

data class BookSearchStudent(
    @JSONField(name = "address")
    var address: String,
    @JSONField(name = "className")
    var className: String,
    @JSONField(name = "deputyGuardianPhone")
    var deputyGuardianPhone: String,
    @JSONField(name = "faceInformation")
    var faceInformation: String,
    @JSONField(name = "id")
    var id: Long,
    @JSONField(name = "isOwnChild")
    var isOwnChild: String,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "phone")
    var phone: String,
    @JSONField(name = "primaryGuardianPhone")
    var primaryGuardianPhone: String,
    @JSONField(name = "sex")
    var sex: String,
    @JSONField(name = "srarchAddressBook")
    var srarchAddressBook: String,
    @JSONField(name = "userId")
    var userId: String
) {

}

data class Parent(
    @JSONField(name = "className")
    var className: String,
    @JSONField(name = "faceInformation")
    var faceInformation: String,
    @JSONField(name = "id")
    var id: Long,
    @JSONField(name = "name")
    var name: String,
    @JSONField(name = "phone")
    var phone: String,
    @JSONField(name = "relation")
    var relation: String,
    @JSONField(name = "singleParent")
    var singleParent: String,
    @JSONField(name = "type")
    var type: String,
    @JSONField(name = "userId")
    var userId: String,
    @JSONField(name = "workUnit")
    var workUnit: String
) {
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
