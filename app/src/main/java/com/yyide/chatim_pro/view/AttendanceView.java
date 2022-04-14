package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AppAddRsp;
import com.yyide.chatim_pro.model.HomeAttendanceRsp;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AttendanceView extends BaseView {
    void getHomeAttendanceListSuccess(HomeAttendanceRsp model);

    void getHomeAttendanceFail(String msg);
}
