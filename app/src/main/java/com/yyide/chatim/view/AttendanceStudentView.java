package com.yyide.chatim.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AttendanceStudentRsp;
import com.yyide.chatim.model.AttendanceTeacherRsp;

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
