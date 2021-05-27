package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AttendanceView extends BaseView {
    void getHomeAttendanceListSuccess(HomeAttendanceRsp model);

    void getHomeAttendanceFail(String msg);
}
