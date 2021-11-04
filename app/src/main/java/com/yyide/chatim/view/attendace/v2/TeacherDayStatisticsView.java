package com.yyide.chatim.view.attendace.v2;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;

public interface TeacherDayStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(TeacherAttendanceDayRsp model);
    void attendanceStatisticsFail(String msg);
}
