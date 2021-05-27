package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AttendanceCheckView extends BaseView {
    void getAttendanceSuccess(AttendanceCheckRsp model);

    void getAttendanceFail(String msg);
}
