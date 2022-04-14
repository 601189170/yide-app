package com.yyide.chatim_pro.view.attendace.v2;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceWeekMonthRsp;

public interface TeacherWeekMonthStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(TeacherAttendanceWeekMonthRsp model);
    void attendanceStatisticsFail(String msg);
}
