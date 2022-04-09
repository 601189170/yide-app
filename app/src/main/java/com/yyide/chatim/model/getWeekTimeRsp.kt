package com.yyide.chatim.model

data class getWeekTimeRsp(
        val afternoonList: List<Afternoon>,
        val earlySelfStudyList: List<EarlySelfStudy>,
        val morningList: List<Morning>,
        val nightList: List<Any>,
        val semesterYear: String,
        val semesterYearName: String,
        val thisWeek: Int,
        val thisWeekEndDate: String,
        val thisWeekStartDate: String,
        val timetableList: List<Timetable>
)

{



data class Afternoon(
    val id: String,
    val name: String,
    val periodType: Int,
    val sequence: Int
)

data class EarlySelfStudy(
    val id: String,
    val name: String,
    val periodType: Int,
    val sequence: Int
)

data class Morning(
    val id: String,
    val name: String,
    val periodType: Int,
    val sequence: Int
)

data class Timetable(
    val id: String,
    val subjectName: String,
    val teacherList: List<Teacher>,
    val week: Int
)

data class Teacher(
    val id: String,
    val name: String
)
}