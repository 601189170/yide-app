package com.yyide.chatim.presenter;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.activity.notice.presenter.NoticeScopePresenter;
import com.yyide.chatim.activity.notice.view.NoticeScopeView;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.databinding.FragmentAttendanceSchoolBinding;
import com.yyide.chatim.databinding.FragmentAttendanceStudentBinding;
import com.yyide.chatim.fragment.AttendanceSchoolFragment;
import com.yyide.chatim.model.AttendanceStudentRsp;
import com.yyide.chatim.model.AttendanceTeacherRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AttendanceStudentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 13:20
 * @Description : 文件描述
 */
public class AttendanceStudentPresenter extends BasePresenter<AttendanceStudentView> {
    public AttendanceStudentPresenter(AttendanceStudentView view) {
        attachView(view);
    }

    public void getAttendStudentBanner(String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        // map.put("startDate", "1970-01-01");
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getStudentAtt(body), new ApiCallback<AttendanceStudentRsp>() {
            @Override
            public void onSuccess(AttendanceStudentRsp rsp) {
                List<AttendanceStudentRsp.AttendanceAdapterBean> bean = null;
                if (!rsp.getData().isEmpty() &&
                        rsp.data.size() > 0) {
                    bean = new ArrayList<>();
                    List<AttendanceStudentRsp.DataDTO> data = rsp.getData();
                    for (int i = 0; i < data.size(); i++) {
                        AttendanceStudentRsp.AttendanceAdapterBean studentatt = new AttendanceStudentRsp.AttendanceAdapterBean();
                        List<AttendanceStudentRsp.DataDTO.CountListDTO> countList;
                        countList = rsp.data.get(i).getCountList();
                        studentatt.name = data.get(i).getName();
                        for (int j = 0; j < countList.size(); j++) {
                            switch (countList.get(j).getAttendanceType()) {
                                case 0:
                                    studentatt.normalNumOut = countList.get(j).getNormalNum();
                                    studentatt.errorNumOut = countList.get(j).getErrorNum();
                                    break;
                                case 1:
                                    studentatt.normalNumEvent = countList.get(j).getNormalNum();
                                    studentatt.errorNumEvent = countList.get(j).getErrorNum();
                                    break;
                                case 2:
                                    studentatt.normalNumCourse = countList.get(j).getNormalNum();
                                    studentatt.errorNumCourse = countList.get(j).getErrorNum();
                                    break;
                            }
                            bean.add(studentatt);
                        }
                    }
                }
                mvpView.getStudentAttendanceSuccess(bean);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getAttendanceFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }

    public void getAttendTeacherBanner(String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        // map.put("startDate", "1970-01-01");
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getTeacherAtt(body), new ApiCallback<AttendanceTeacherRsp>() {
            @Override
            public void onSuccess(AttendanceTeacherRsp rsp) {
                List<AttendanceTeacherRsp.AttendanceTeacherAdapterBean> teacherList = null;
                if (!rsp.getData().isEmpty() &&
                        rsp.getData().size() > 0) {
                    List<AttendanceTeacherRsp.DataDTO> data = rsp.getData();
                    teacherList = new ArrayList<>();
                    AttendanceTeacherRsp.AttendanceTeacherAdapterBean teacherbean = new AttendanceTeacherRsp.AttendanceTeacherAdapterBean();
                    for (int i = 0; i < data.size(); i++) {
                        switch (data.get(i).getAttendanceType()) {
                            case 0:
                                teacherbean.normalRateOut = data.get(i).getNormalRate();
                                break;
                            case 1:
                                teacherbean.normalRateTime = data.get(i).getNormalRate();
                                break;
                            case 2:
                                teacherbean.normalRateCourse = data.get(i).getNormalRate();
                                break;
                        }
                    }
                    teacherList.add(teacherbean);
                }
                mvpView.getTeacherAttendanceSuccess(teacherList);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getAttendanceFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }
}
