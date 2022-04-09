package com.yyide.chatim.model

data class getSchoolWorkFeedback(
        val classesName: String,
        val completion: Int,
        val difficulty: Int,
        val endTime: String,
        val id: String,
        val isFeedback: Boolean,
        val isRead: Boolean,
        val sequence: String,
        val startTime: String,
        val subjectName: String,
        val week: String,
        val weekTime: String
)

