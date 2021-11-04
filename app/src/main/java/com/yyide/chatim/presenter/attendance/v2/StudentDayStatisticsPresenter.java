package com.yyide.chatim.presenter.attendance.v2;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.attendance.StudentAttendanceDayRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.attendace.v2.StudentDayStatisticsView;
import com.yyide.chatim.view.attendace.v2.TeacherDayStatisticsView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class StudentDayStatisticsPresenter extends BasePresenter<StudentDayStatisticsView> {
    private static final String TAG = StudentDayStatisticsPresenter.class.getSimpleName();

    public StudentDayStatisticsPresenter(StudentDayStatisticsView view) {
        attachView(view);
    }

    /**
     * 家长查看学生日统计考勤数据
     *
     * @param classId
     * @param startTime
     * @param endTime
     */
    public void queryAppStudentAttendanceDay(String classId, String studentId, String startTime, String endTime) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        map.put("studentId", studentId);
        map.put("queryType", 1);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        final String json = JSON.toJSONString(map);
        Log.e(TAG, "queryAppStudentAttendanceDay: " + json);
        RequestBody body = RequestBody.create(BaseConstant.JSON, json);
        addSubscription(dingApiStores.queryAppStudentAttendanceDay(body), new ApiCallback<StudentAttendanceDayRsp>() {
            @Override
            public void onSuccess(StudentAttendanceDayRsp model) {
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
