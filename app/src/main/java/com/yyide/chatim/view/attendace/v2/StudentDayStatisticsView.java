package com.yyide.chatim.view.attendace.v2;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.attendance.StudentAttendanceDayRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;

public interface StudentDayStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(StudentAttendanceDayRsp model);
    void attendanceStatisticsFail(String msg);
}
