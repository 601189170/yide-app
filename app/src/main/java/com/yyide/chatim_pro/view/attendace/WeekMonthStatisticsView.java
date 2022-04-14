package com.yyide.chatim_pro.view.attendace;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AttendanceWeekStatsRsp;
public interface WeekMonthStatisticsView extends BaseView {
    void attendanceStatisticsSuccess(AttendanceWeekStatsRsp model);
    void attendanceStatisticsFail(String msg);
}
