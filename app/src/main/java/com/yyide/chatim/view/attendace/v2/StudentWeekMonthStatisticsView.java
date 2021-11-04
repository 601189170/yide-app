package com.yyide.chatim.view.attendace.v2;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.attendance.StudentAttendanceWeekMonthRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceWeekMonthRsp;

public interface StudentWeekMonthStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(StudentAttendanceWeekMonthRsp model);
    void attendanceStatisticsFail(String msg);
}
