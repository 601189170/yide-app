package com.yyide.chatim_pro.presenter.attendance;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.AttendanceDayStatsRsp;
import com.yyide.chatim_pro.model.AttendanceWeekStatsRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.attendace.DayStatisticsView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class DayStatisticsPresenter extends BasePresenter<DayStatisticsView> {
    private static final String TAG = DayStatisticsPresenter.class.getSimpleName();
    public DayStatisticsPresenter(DayStatisticsView view) {
        attachView(view);
    }

    /**
     * 获取考勤统计
     * @param classId 班级id
     * @param seacherTime 日期
     * @param dateType 日期类型
     */
    public void getAttendanceStatsData(String studentId,String classId,String seacherTime,String dateType) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        if (!SpData.getIdentityInfo().staffIdentity()) {
            map.put("studentId", studentId);
        }
        map.put("seacherTime", seacherTime);
        map.put("dateType", dateType);
        final String json = JSON.toJSONString(map);
        Log.e(TAG, "getAttendanceStatsData: "+ json);
        RequestBody body = RequestBody.create(BaseConstant.JSON, json);
        addSubscription(dingApiStores.viewAttDayStatistics(body), new ApiCallback<AttendanceDayStatsRsp>() {
            @Override
            public void onSuccess(AttendanceDayStatsRsp model) {
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
