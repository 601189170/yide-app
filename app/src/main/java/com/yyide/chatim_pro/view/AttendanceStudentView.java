package com.yyide.chatim_pro.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AttendanceStudentRsp;
import com.yyide.chatim_pro.model.AttendanceTeacherRsp;

import java.util.List;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 13:21
 * @Description : 文件描述
 */
public interface AttendanceStudentView extends BaseView {

    void getAttendanceFail(String rsp);

    //家长端查看考勤
    void getStudentAttendanceSuccess(List<AttendanceStudentRsp.AttendanceAdapterBean> rsp);

    //教师端查看考勤
    void getTeacherAttendanceSuccess(List<AttendanceTeacherRsp.AttendanceTeacherAdapterBean> rsp);
}
