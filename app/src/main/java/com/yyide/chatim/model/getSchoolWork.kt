package com.yyide.chatim.model

data class getSchoolWork(
        val classesTimetable:ClassesTimetable,
        val content: String,
        val feedbackEndTime: String,
        val feedbackList: List<Feedback>,
        val id: String,
        val imgPaths: String,
        val isScheduled: Boolean,
        val noFeedbackList: List<NoFeedback>,
        val noReadList: List<NoRead>,
        val publisherId: String,
        val readList: List<Read>,
        val releaseTime: String,
        val schoolId: String,
        val subjectId: String,
        val title: String
)
   {
        data class ClassesTimetable(
                val classesId: String,
                val classesName: String,
                val endTime: String,
                val id: String,
                val sequence: String,
                val startTime: String,
                val subjectName: String,
                val week: String,
                val weekTime: String,
                val workId: String
        )

        data class Feedback(
                val completion: Int,
                val difficulty: Int,
                val isFeedback: Boolean,
                val isRead: Boolean,
                val isRemind: Boolean,
                val studentId: String,
                val studentName: String
        )

        data class NoFeedback(
                val isFeedback: Boolean,
                val isRead: Boolean,
                val isRemind: Boolean,
                val studentId: String,
                val studentName: String
        )

        data class NoRead(
                val completion: Int,
                val difficulty: Int,
                val isFeedback: Boolean,
                val isRead: Boolean,
                val isRemind: Boolean,
                val studentId: String,
                val studentName: String
        )

        data class Read(
                val completion: Int,
                val difficulty: Int,
                val isFeedback: Boolean,
                val isRead: Boolean,
                val isRemind: Boolean,
                val studentId: String,
                val studentName: String
        )

}

