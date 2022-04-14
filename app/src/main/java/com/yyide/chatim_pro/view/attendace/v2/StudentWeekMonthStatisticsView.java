package com.yyide.chatim_pro.view.attendace.v2;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceWeekMonthRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceWeekMonthRsp;

public interface StudentWeekMonthStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(StudentAttendanceWeekMonthRsp model);
    void attendanceStatisticsFail(String msg);
}
