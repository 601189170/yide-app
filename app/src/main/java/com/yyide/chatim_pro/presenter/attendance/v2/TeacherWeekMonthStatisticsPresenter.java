package com.yyide.chatim_pro.presenter.attendance.v2;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.AttendanceDayStatsRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceWeekMonthRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.attendace.DayStatisticsView;
import com.yyide.chatim_pro.view.attendace.v2.TeacherWeekMonthStatisticsView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class TeacherWeekMonthStatisticsPresenter extends BasePresenter<TeacherWeekMonthStatisticsView> {
    private static final String TAG = TeacherWeekMonthStatisticsPresenter.class.getSimpleName();

    public TeacherWeekMonthStatisticsPresenter(TeacherWeekMonthStatisticsView view) {
        attachView(view);
    }

    /**
     * 班主任或任课老师查看周、月统计信息
     *
     * @param classId
     * @param startTime
     * @param endTime
     */
    public void queryAppTeacherThreeAttendanceWeekMonth(String classId, String startTime, String endTime) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        map.put("queryType", 2);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        final String json = JSON.toJSONString(map);
        Log.e(TAG, "getAttendanceStatsData: " + json);
        RequestBody body = RequestBody.create(BaseConstant.JSON, json);
        addSubscription(dingApiStores.queryAppTeacherThreeAttendanceWeekMonth(body), new ApiCallback<TeacherAttendanceWeekMonthRsp>() {
            @Override
            public void onSuccess(TeacherAttendanceWeekMonthRsp model) {
                mvpView.attendanceStatisticsSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.attendanceStatisticsFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
