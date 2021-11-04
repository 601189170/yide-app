package com.yyide.chatim.presenter.attendance.v2;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.attendace.v2.TeacherDayStatisticsView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class TeacherDayStatisticsPresenter extends BasePresenter<TeacherDayStatisticsView> {
    private static final String TAG = TeacherDayStatisticsPresenter.class.getSimpleName();

    public TeacherDayStatisticsPresenter(TeacherDayStatisticsView view) {
        attachView(view);
    }

    /**
     * 班主任或任课老师查看日统计信息
     * @param classId
     * @param startTime
     * @param endTime
     */
    public void queryAppTeacherThreeAttendanceDay(String classId, String startTime, String endTime) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        map.put("queryType", 1);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        final String json = JSON.toJSONString(map);
        Log.e(TAG, "getAttendanceStatsData: " + json);
        RequestBody body = RequestBody.create(BaseConstant.JSON, json);
        addSubscription(dingApiStores.queryAppTeacherThreeAttendanceDay(body), new ApiCallback<TeacherAttendanceDayRsp>() {
            @Override
            public void onSuccess(TeacherAttendanceDayRsp model) {
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
