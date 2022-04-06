package com.yyide.chatim.model

data class TeacherWorkListRsp(
    val code: Int,
    val `data`: DataB,
    val message: String
) {

    data class DataB(
            val list: List<WorkInfo>,
            val pageNo: Int,
            val pageSize: Int,
            val total: Int
    ){
        data class WorkInfo(
                val completed: Int,
                val id: String,
                val isScheduled: Boolean,
                val read: Int,
                val releaseTime: String,
                val subjectName: String,
                val title: String,
                val total: Int
        )
    }

}

