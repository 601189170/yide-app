package com.yyide.chatim_pro.view.attendace.v2;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AttendanceDayStatsRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceDayRsp;

public interface TeacherDayStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(TeacherAttendanceDayRsp model);
    void attendanceStatisticsFail(String msg);
}
