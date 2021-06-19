package com.yyide.chatim.view.attendace;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
public interface WeekMonthStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(AttendanceWeekStatsRsp model);
    void attendanceStatisticsFail(String msg);
}
