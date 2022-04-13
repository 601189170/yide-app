package com.yyide.chatim.model

data class getSchoolWorkFeedback(
        val classesTimetable: ClassesTimetable,
        val content: String,
        val feedbackData: FeedbackData,
        val feedbackEndTime: String,
        val feedbackList: List<Any>,
        val id: String,
        val imgPaths: String,
        val isScheduled: Boolean,
        val noFeedbackList: List<Any>,
        val noReadList: List<Any>,
        val readList: List<Any>,
        val releaseTime: String,
        val timetableTime: String,
        val title: String
)


data class ClassesTimetable(
    val classesId: String,
    val classesName: String,
    val id: String,
    val subjectName: String,
    val timetableTime: String?="",
    val startTime: String,
    val sequence: String
)

data class FeedbackData(
    val completion: Int,
    val difficulty: Int,
    val isFeedback: Boolean,
    val isRead: Boolean,
    val studentId: String
)