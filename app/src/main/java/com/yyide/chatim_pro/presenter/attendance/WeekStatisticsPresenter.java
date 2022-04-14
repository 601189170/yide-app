package com.yyide.chatim_pro.presenter.attendance;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.AttendanceWeekStatsRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.attendace.WeekMonthStatisticsView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class WeekStatisticsPresenter extends BasePresenter<WeekMonthStatisticsView> {
    private static final String TAG = WeekStatisticsPresenter.class.getSimpleName();
    public WeekStatisticsPresenter(WeekMonthStatisticsView view) {
        attachView(view);
    }

    /**
     * 获取考勤统计
     * @param classId 班级id
     * @param seacherTime 日期
     * @param dateType 日期类型
     */
    public void getAttendanceStatsData(String studentId,String classId,String seacherTime,String dateType,int page) {
        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        if (!SpData.getIdentityInfo().staffIdentity()) {
            map.put("studentId", studentId);
        }
        map.put("seacherTime", seacherTime);
        map.put("dateType", dateType);
        map.put("page",page);
        final String json = JSON.toJSONString(map);
        Log.e(TAG, "getAttendanceStatsData: "+ json);
        RequestBody body = RequestBody.create(BaseConstant.JSON, json);
        addSubscription(dingApiStores.viewAttWeekMonthStatistics(body), new ApiCallback<AttendanceWeekStatsRsp>() {
            @Override
            public void onSuccess(AttendanceWeekStatsRsp model) {
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
