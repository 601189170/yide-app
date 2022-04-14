package com.yyide.chatim_pro.presenter.attendance.v2;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceDayRsp;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceWeekMonthRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.attendace.v2.StudentDayStatisticsView;
import com.yyide.chatim_pro.view.attendace.v2.StudentWeekMonthStatisticsView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class StudentWeekMonthStatisticsPresenter extends BasePresenter<StudentWeekMonthStatisticsView> {
    private static final String TAG = StudentWeekMonthStatisticsPresenter.class.getSimpleName();

    public StudentWeekMonthStatisticsPresenter(StudentWeekMonthStatisticsView view) {
        attachView(view);
    }

    /**
     * 家长查看学生周、月考勤数据
     *
     * @param classId
     * @param startTime
     * @param endTime
     */
    public void queryAppStudentAttendanceWeekMonth(String classId, String studentId, String startTime, String endTime) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        map.put("studentId", studentId);
        map.put("queryType", 2);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        final String json = JSON.toJSONString(map);
        Log.e(TAG, "queryAppStudentAttendanceDay: " + json);
        RequestBody body = RequestBody.create(BaseConstant.JSON, json);
        addSubscription(dingApiStores.queryAppStudentAttendanceWeekMonth(body), new ApiCallback<StudentAttendanceWeekMonthRsp>() {
            @Override
            public void onSuccess(StudentAttendanceWeekMonthRsp model) {
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
