package com.yyide.chatim.view.attendace;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;

public interface DayStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(AttendanceDayStatsRsp model);
    void attendanceStatisticsFail(String msg);
}
