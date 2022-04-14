package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AttendanceCheckRsp;
import com.yyide.chatim_pro.model.AttendanceRsp;
import com.yyide.chatim_pro.model.HomeAttendanceRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AttendanceCheckView extends BaseView {
    void getAttendanceSuccess(AttendanceRsp model);

    void getAttendanceFail(String msg);
}
