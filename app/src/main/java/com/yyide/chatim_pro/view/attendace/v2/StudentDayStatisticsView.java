package com.yyide.chatim_pro.view.attendace.v2;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceDayRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceDayRsp;

public interface StudentDayStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(StudentAttendanceDayRsp model);
    void attendanceStatisticsFail(String msg);
}
