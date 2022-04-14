package com.yyide.chatim_pro.view.attendace;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AttendanceDayStatsRsp;
import com.yyide.chatim_pro.model.AttendanceWeekStatsRsp;

public interface DayStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(AttendanceDayStatsRsp model);
    void attendanceStatisticsFail(String msg);
}
