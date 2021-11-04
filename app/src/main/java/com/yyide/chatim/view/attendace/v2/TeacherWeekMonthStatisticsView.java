package com.yyide.chatim.view.attendace.v2;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceWeekMonthRsp;

public interface TeacherWeekMonthStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(TeacherAttendanceWeekMonthRsp model);
    void attendanceStatisticsFail(String msg);
}
